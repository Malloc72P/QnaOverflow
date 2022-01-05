package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.service.exception.question.delete.QuestionDeleteFailedException;
import scra.qnaboard.service.exception.question.edit.QuestionEditFailedException;
import scra.qnaboard.service.exception.question.edit.QuestionPropertyIsEmptyException;
import scra.qnaboard.service.exception.question.edit.UnauthorizedQuestionEditException;
import scra.qnaboard.utils.EntityConverter;
import scra.qnaboard.utils.QueryUtils;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static scra.qnaboard.utils.QueryUtils.isDeletedPost;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private QuestionService questionService;

    /**
     * 서비스의 질문상세보기 기능을 테스트함
     */
    @Test
    @DisplayName("아이디로 질문글 엔티티를 찾고 DTO로 변환할 수 있어야 함")
    void testQuestionDetail() {
        Question[] questions = TestDataInit.init(em).getQuestions();

        em.flush();
        em.clear();

        Arrays.stream(questions).forEach(question -> {
            QuestionDetailDTO detailDTO = questionService.questionDetail(question.getId());
            testDetailDTO(detailDTO, question);
        });
    }

    /**
     * DTO가 엔티티의 값을 잘 가지고 있는지 테스트함
     */
    private void testDetailDTO(QuestionDetailDTO detailDTO, Question question) {
        assertThat(detailDTO).extracting(
                QuestionDetailDTO::getQuestionId,
                QuestionDetailDTO::getTitle,
                QuestionDetailDTO::getContent,
                QuestionDetailDTO::getVoteScore,
                QuestionDetailDTO::getViewCount,
                QuestionDetailDTO::getAuthorName
        ).containsExactly(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getUpVoteCount() - question.getDownVoteCount(),
                question.getViewCount(),
                question.getAuthor().getNickname()
        );

        int sizeOfQuestionTag = QueryUtils.sizeOfQuestionTagsByQuestionId(em, question.getId());
        assertThat(detailDTO.getTags().size()).isEqualTo(sizeOfQuestionTag);
        int sizeOfAnswer = QueryUtils.sizeOfAnswerByQuestionId(em, question.getId());
        assertThat(detailDTO.getAnswers().size()).isEqualTo(sizeOfAnswer);
    }

    @Test
    @DisplayName("작성자는 질문글을 삭제할 수 있어야 함")
    void authorCanDeleteOwnQuestion() {
        Question[] questions = TestDataInit.init(em).getQuestions();

        em.flush();
        em.clear();

        for (Question question : questions) {
            questionService.deleteQuestion(question.getAuthor().getId(), question.getId());
            assertThat(isDeletedPost(em, question)).isTrue();
        }
    }

    @Test
    @DisplayName("관리자는 모든 질문글을 삭제할 수 있어야 함")
    void adminCanDeleteAllQuestion() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Question[] questions = dataDTO.getQuestions();
        Member admin = dataDTO.adminMember();

        em.flush();
        em.clear();

        for (Question question : questions) {
            questionService.deleteQuestion(admin.getId(), question.getId());
            assertThat(isDeletedPost(em, question)).isTrue();
        }
    }

    @Test
    @DisplayName("관리자가 아닌 사용자는 다른 사용자의 질문글을 지울 수 없어야 함")
    void memberCanNotDeleteOtherMembersQuestion() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Question[] questions = dataDTO.getQuestions();

        em.flush();
        em.clear();

        for (Question question : questions) {
            Member author = question.getAuthor();
            Member anotherMember = dataDTO.anotherMemberAndNotAdmin(author);
            assertThatThrownBy(() -> questionService.deleteQuestion(anotherMember.getId(), question.getId()))
                    .isInstanceOf(QuestionDeleteFailedException.class);
        }
    }

    @Test
    @DisplayName("자기가 작성한 게시글을 수정할 수 있어야 함")
    void memberCanEditOwnQuestion() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Question[] questions = dataDTO.getQuestions();

        em.flush();
        em.clear();

        for (Question question : questions) {
            Member author = question.getAuthor();
            String editTitle = "edited-title-" + question.getId();
            String editContent = "edited-content-" + question.getId();
            List<Long> tagIds = EntityConverter.getTagIds(question);

            questionService.editQuestion(author.getId(), question.getId(), editTitle, editContent, tagIds);

            Question findQuestion = em.createQuery("select q from Question q where q.id = :id", Question.class)
                    .setParameter("id", question.getId())
                    .getSingleResult();

            assertThat(findQuestion).extracting(
                    Question::getTitle,
                    Question::getContent
            ).containsExactly(
                    editTitle,
                    editContent
            );
        }
    }

    @Test
    @DisplayName("자기가 작성한 게시글을 수정할 수 있어야 함")
    void adminCanEditAllQuestion() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Question[] questions = dataDTO.getQuestions();
        Member admin = dataDTO.adminMember();

        em.flush();
        em.clear();

        for (Question question : questions) {
            String editTitle = "edited-title-" + question.getId();
            String editContent = "edited-content-" + question.getId();
            List<Long> tagIds = EntityConverter.getTagIds(question);

            questionService.editQuestion(admin.getId(), question.getId(), editTitle, editContent, tagIds);

            Question findQuestion = em.createQuery("select q from Question q where q.id = :id", Question.class)
                    .setParameter("id", question.getId())
                    .getSingleResult();

            assertThat(findQuestion).extracting(
                    Question::getTitle,
                    Question::getContent
            ).containsExactly(
                    editTitle,
                    editContent
            );
        }
    }

    @Test
    @DisplayName("일반 유저는 다른 사용자의 질문글을 수정할 수 없어야 함")
    void memberCanNotEditAnotherMembersQuestion() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Question[] questions = dataDTO.getQuestions();

        em.flush();
        em.clear();

        for (Question question : questions) {
            Member author = question.getAuthor();
            Long anotherMemberId = dataDTO.anotherMemberAndNotAdmin(author).getId();
            String editTitle = "edited-title-" + question.getId();
            String editContent = "edited-content-" + question.getId();
            List<Long> tagIds = EntityConverter.getTagIds(question);

            assertThatThrownBy(() -> questionService.editQuestion(anotherMemberId, question.getId(), editTitle, editContent, tagIds))
                    .isInstanceOf(QuestionEditFailedException.class)
                    .isInstanceOf(UnauthorizedQuestionEditException.class);

            Question findQuestion = em.createQuery("select q from Question q where q.id = :id", Question.class)
                    .setParameter("id", question.getId())
                    .getSingleResult();

            assertThat(findQuestion.getTitle()).isNotEqualTo(editTitle);
            assertThat(findQuestion.getContent()).isNotEqualTo(editContent);
        }
    }

    @Test
    @DisplayName("질문글의 내용을 빈 내용으로 수정할 수 없어야함")
    void canNotEditQuestionWithEmptyProperty() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Question[] questions = dataDTO.getQuestions();

        em.flush();
        em.clear();

        String[][] testcases = {
                {"", ""},
                {"a", ""},
                {"", "a"}
        };

        Question question = questions[0];
        Long authorId = question.getAuthor().getId();
        for (String[] testcase : testcases) {
            assertThatThrownBy(() -> questionService.editQuestion(authorId, question.getId(), testcase[0], testcase[1], new ArrayList<>()))
                    .isInstanceOf(QuestionEditFailedException.class)
                    .isInstanceOf(QuestionPropertyIsEmptyException.class);
        }
    }

}
