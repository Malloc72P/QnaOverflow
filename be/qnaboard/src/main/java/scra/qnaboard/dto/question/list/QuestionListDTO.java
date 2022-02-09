package scra.qnaboard.dto.question.list;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 질문 목록조회를 위한 DTO. QuestionSummaryDTO를 컬렉션으로 가진다
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
