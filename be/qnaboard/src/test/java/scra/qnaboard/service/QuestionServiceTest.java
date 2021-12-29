package scra.qnaboard.service;

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

        assertThat(detailDTO.getTags().size()).isEqualTo(question.getQuestionTags().size());
        assertThat(detailDTO.getAnswers().size()).isEqualTo(question.getAnswers().size());
    }
}
