package scra.qnaboard.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.QuestionTag;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.utils.TestDataInit;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionSearchRepositoryTest {

    @Autowired
    private QuestionSearchRepository repository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("질문 상세보기를 하면 질문과 연관된 작성자, 답변글목록, 태그목록을 가져와야 합니다")
    void questionDetail() {
        Question testTargetQuestion = TestDataInit.init(em);

        em.flush();
        em.clear();

        testDetailEntity(testTargetQuestion);

    }

    /**
     * 예상되는 값을 가지고 엔티티를 테스트.
     *
     * @param testTargetQuestion 예상되는 엔티티의 상태값
     */
    private void testDetailEntity(Question testTargetQuestion) {
        Question findQuestion = repository.questionDetail(testTargetQuestion.getId())
                .orElse(null);

        //찾은 question 엔티티는 null 이면 안됨
        assertThat(findQuestion).isNotNull();

        //태그와 답변글 개수가 같아야 함
        assertThat(findQuestion.getQuestionTags().size()).isEqualTo(testTargetQuestion.getQuestionTags().size());
        assertThat(findQuestion.getAnswers().size()).isEqualTo(testTargetQuestion.getAnswers().size());

        //제목, 내용, 작성자가 같아야 함
        assertThat(findQuestion)
                .extracting(
                        Question::getTitle,
                        Question::getContent,
                        Question::getAuthor
                ).containsExactly(
                        testTargetQuestion.getTitle(),
                        testTargetQuestion.getContent(),
                        testTargetQuestion.getAuthor()
                );

        //태그 내용이 같아야 함
        assertThat(findQuestion.getQuestionTags())
                .contains(testTargetQuestion.getQuestionTags().toArray(new QuestionTag[0]));

        //답변글 내용이 같아야 함
        assertThat(findQuestion.getAnswers())
                .contains(testTargetQuestion.getAnswers().toArray(new Answer[0]));
    }

}
