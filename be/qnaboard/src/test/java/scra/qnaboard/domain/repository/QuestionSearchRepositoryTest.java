package scra.qnaboard.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.utils.TestDataInit;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionSearchRepositoryTest {

    @Autowired
    private QuestionSearchRepository repository;

    @Autowired
    private EntityManager em;

    /**
     * 패치조인으로 질문상세보기하는 메서드를 테스트함
     */
    @Test
    @DisplayName("질문 상세보기를 할 수 있어야 함")
    void questionDetail() {
        Question[] questions = TestDataInit.init(em);

        em.flush();
        em.clear();

        Arrays.stream(questions)
                .forEach(question -> {
                    Question findQuestion = repository.questionDetail(question.getId()).orElse(null);
                    testFoundEntityIsEqualToExpected(question, findQuestion);
                });
    }

    /**
     * 프로젝션으로 질문 상세보기하는 메서드를 테스트함
     */
    @Test
    @DisplayName("질문 상세보기를 v2메서드로 할 수 있어야 함")
    void questionDetailV2() {
        Question[] questions = TestDataInit.init(em);

        em.flush();
        em.clear();

        Arrays.stream(questions)
                .forEach(question -> {
                    QuestionDetailDTO detailDTO = repository.questionDetailV2(question.getId());
                    testDetailDTO(detailDTO, question);
                });
    }

    /**
     * 예상되는 값을 가지고 엔티티를 테스트.
     *
     * @param question 예상되는 엔티티의 상태값
     */
    private void testFoundEntityIsEqualToExpected(Question question, Question findQuestion) {
        //찾은 question 엔티티는 null 이면 안됨
        assertThat(findQuestion).isNotNull();

        //태그와 답변글 개수가 같아야 함
        assertThat(findQuestion.getQuestionTags().size()).isEqualTo(question.getQuestionTags().size());
        assertThat(findQuestion.getAnswers().size()).isEqualTo(question.getAnswers().size());

        //제목, 내용, 작성자가 같아야 함
        assertThat(findQuestion)
                .extracting(
                        Question::getTitle,
                        Question::getContent,
                        Question::getAuthor
                ).containsExactly(
                        question.getTitle(),
                        question.getContent(),
                        question.getAuthor()
                );

        //태그와 답변글 개수가 같아야 함
        assertThat(findQuestion.getQuestionTags().size()).isEqualTo(question.getQuestionTags().size());
        assertThat(findQuestion.getAnswers().size()).isEqualTo(question.getAnswers().size());
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

        assertThat(detailDTO.getTags().size()).isEqualTo(question.getQuestionTags().size());
        assertThat(detailDTO.getAnswers().size()).isEqualTo(question.getAnswers().size());
    }
}
