package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.service.exception.answer.delete.AnswerDeleteFailedException;
import scra.qnaboard.service.exception.answer.edit.AnswerEditFailedException;
import scra.qnaboard.service.exception.answer.edit.AnswerPropertyIsEmptyException;
import scra.qnaboard.service.exception.answer.edit.UnauthorizedAnswerEditException;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static scra.qnaboard.utils.QueryUtils.isDeletedPost;

@SpringBootTest
@Transactional
class AnswerServiceTest {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("AnswerService로 답변게시글을 생성할 수 있어야 함")
    void testCreateAnswer() {
        TestDataDTO testData = TestDataInit.init(em);

        Member author = Arrays.stream(testData.getMembers())
                .filter(Member::isNotAdmin)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다! : member"));

        Question question = Arrays.stream(testData.getQuestions())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다! : question"));

        AnswerDetailDTO createAnswerDTO = answerService.createAnswer(author.getId(), question.getId(), "content-1");
        Answer findAnswer = em.createQuery("select a from Answer a where a.id = :id", Answer.class)
                .setParameter("id", createAnswerDTO.getAnswerId())
                .getSingleResult();

        assertThat(createAnswerDTO).extracting(
                AnswerDetailDTO::getAnswerId,
                AnswerDetailDTO::getContent,
                AnswerDetailDTO::getCreatedDate,
                AnswerDetailDTO::getLastModifiedDate,
                AnswerDetailDTO::getVoteScore,
                AnswerDetailDTO::getAuthorId,
                AnswerDetailDTO::getAuthorName
        ).containsExactly(
                findAnswer.getId(),
                findAnswer.getContent(),
                findAnswer.getCreatedDate(),
                findAnswer.getLastModifiedDate(),
                0L,
                findAnswer.getAuthor().getId(),
                findAnswer.getAuthor().getNickname()
        );
    }

    @Test
    @DisplayName("답변게시글을 수정할 수 있어야 함")
    void authorCanEditOwnAnswer() {
        TestDataDTO testData = TestDataInit.init(em);

        Answer[] answers = testData.getAnswers();
        for (Answer answer : answers) {
            String newContent = "aaaaaaaaaaaaaaaaaaa";
            Member author = answer.getAuthor();
            answerService.editAnswer(author.getId(), answer.getId(), newContent);

            Answer findAnswer = em.createQuery("select a from Answer a where a.id = :id", Answer.class)
                    .setParameter("id", answer.getId())
                    .getSingleResult();

            assertThat(findAnswer.getContent()).isEqualTo(newContent);
        }
    }

    @Test
    @DisplayName("다른 사용자가 작성한 답변게시글을 수정할 수 없어야 함")
    void memberCanNotEditOtherMembersAnswer() {
        TestDataDTO testData = TestDataInit.init(em);

        Answer[] answers = testData.getAnswers();
        for (Answer answer : answers) {
            String prevContent = answer.getContent();
            String newContent = "aaaaaaaaaaaaaaaaaaa";
            Member author = answer.getAuthor();
            Member otherMember = testData.anotherMemberAndNotAdmin(author);

            assertThatThrownBy(() -> answerService.editAnswer(otherMember.getId(), answer.getId(), newContent))
                    .isInstanceOf(AnswerEditFailedException.class)
                    .isInstanceOf(UnauthorizedAnswerEditException.class);

            Answer findAnswer = em.createQuery("select a from Answer a where a.id = :id", Answer.class)
                    .setParameter("id", answer.getId())
                    .getSingleResult();

            assertThat(findAnswer.getContent())
                    .isNotEqualTo(newContent)
                    .isEqualTo(prevContent);
        }
    }

    @Test
    @DisplayName("관리자는 모든 답변게시글을 수정할 수 있어야 함")
    void adminCanEditAllAnswer() {
        TestDataDTO testData = TestDataInit.init(em);
        Member admin = testData.adminMember();

        Answer[] answers = testData.getAnswers();
        for (Answer answer : answers) {
            String newContent = "aaaaaaaaaaaaaaaaaaa";

            answerService.editAnswer(admin.getId(), answer.getId(), newContent);

            Answer findAnswer = em.createQuery("select a from Answer a where a.id = :id", Answer.class)
                    .setParameter("id", answer.getId())
                    .getSingleResult();

            assertThat(findAnswer.getContent())
                    .isEqualTo(newContent);
        }
    }

    @Test
    @DisplayName("답변게시글을 빈 내용으로 수정할 수 없어야 함")
    void answerCanNotBeEditedWithEmptyContent() {
        TestDataDTO testData = TestDataInit.init(em);

        Answer[] answers = testData.getAnswers();
        for (Answer answer : answers) {
            String prevContent = answer.getContent();
            String newContent = "";
            Member author = answer.getAuthor();

            assertThatThrownBy(() -> answerService.editAnswer(author.getId(), answer.getId(), newContent))
                    .isInstanceOf(AnswerEditFailedException.class)
                    .isInstanceOf(AnswerPropertyIsEmptyException.class);

            Answer findAnswer = em.createQuery("select a from Answer a where a.id = :id", Answer.class)
                    .setParameter("id", answer.getId())
                    .getSingleResult();

            assertThat(findAnswer.getContent())
                    .isNotEqualTo(newContent)
                    .isEqualTo(prevContent);
        }
    }

    @Test
    @DisplayName("작성자는 답변글을 삭제할 수 있어야 함")
    void authorCanDeleteOwnAnswer() {
        Answer[] answers = TestDataInit.init(em).getAnswers();

        for (Answer answer : answers) {
            answerService.deleteAnswer(answer.getAuthor().getId(), answer.getId());
            assertThat(isDeletedPost(em, answer)).isTrue();
        }
    }

    @Test
    @DisplayName("관리자는 모든 답변글을 삭제할 수 있어야 함")
    void adminCanDeleteAllAnswer() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Answer[] answers = dataDTO.getAnswers();
        Member admin = dataDTO.adminMember();

        for (Answer answer : answers) {
            answerService.deleteAnswer(admin.getId(), answer.getId());
            assertThat(isDeletedPost(em, answer)).isTrue();
        }
    }

    @Test
    @DisplayName("관리자가 아닌 사용자는 다른 사용자의 답변글을 지울 수 없어야 함")
    void memberCanNotDeleteOtherMembersAnswer() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Answer[] answers = dataDTO.getAnswers();
        Member admin = dataDTO.adminMember();

        for (Answer answer : answers) {
            Member author = answer.getAuthor();
            Member anotherMember = dataDTO.anotherMemberAndNotAdmin(author);
            assertThatThrownBy(() -> answerService.deleteAnswer(anotherMember.getId(), answer.getId()))
                    .isInstanceOf(AnswerDeleteFailedException.class);
        }
    }
}
