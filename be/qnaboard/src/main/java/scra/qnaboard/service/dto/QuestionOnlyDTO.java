package scra.qnaboard.service.dto;

import lombok.Getter;
import scra.qnaboard.domain.entity.post.Question;

@Getter
public class QuestionOnlyDTO {

    private String title;
    private String content;

    public static QuestionOnlyDTO from(Question question) {
        QuestionOnlyDTO dto = new QuestionOnlyDTO();
        dto.title = question.getTitle();
        dto.content = question.getContent();
        return dto;
    }
}
