package scra.qnaboard.web.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
