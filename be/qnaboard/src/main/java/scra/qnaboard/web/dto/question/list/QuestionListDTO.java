package scra.qnaboard.web.dto.question.list;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 질문 목록조회를 위한 DTO. QuestionSummaryDTO를 컬렉션으로 가진다
 */
@Getter
public class QuestionListDTO {
    private List<QuestionSummaryDTO> questions = new ArrayList<>();

    public static QuestionListDTO from(List<QuestionSummaryDTO> summaryDTOS) {
        QuestionListDTO dto = new QuestionListDTO();
        summaryDTOS.forEach(dto::addQuestion);
        return dto;
    }

    private void addQuestion(QuestionSummaryDTO questionSummaryDTO) {
        this.questions.add(questionSummaryDTO);
    }
}
