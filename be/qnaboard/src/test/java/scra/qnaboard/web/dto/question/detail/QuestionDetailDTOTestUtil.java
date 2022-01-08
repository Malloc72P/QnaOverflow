package scra.qnaboard.web.dto.question.detail;

import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.utils.QueryUtils;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDetailDTOTestUtil {

    public static void testQuestionDetailDTO(EntityManager em,
                                             Question question,
                                             QuestionDetailDTO detailDTO) {
        assertThat(detailDTO).extracting(
                QuestionDetailDTO::getQuestionId,
                QuestionDetailDTO::getTitle,
                QuestionDetailDTO::getContent,
                QuestionDetailDTO::getViewCount,
                QuestionDetailDTO::getAuthorName
        ).containsExactly(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getViewCount(),
                question.getAuthor().getNickname()
        );

        int sizeOfQuestionTag = QueryUtils.sizeOfQuestionTagsByQuestionId(em, question.getId());
        int sizeOfAnswer = QueryUtils.sizeOfAnswerByQuestionId(em, question.getId());

        assertThat(detailDTO.getTags().size()).isEqualTo(sizeOfQuestionTag);
        assertThat(detailDTO.getAnswers().size()).isEqualTo(sizeOfAnswer);
    }
}
