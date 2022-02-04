package scra.qnaboard.web.dto.question.create;

import lombok.Getter;
import lombok.ToString;
import scra.qnaboard.web.dto.question.QuestionTagForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
public class CreateQuestionForm extends QuestionTagForm {

    @NotBlank
    @Size(min = 6, max = 200)
    private String title;

    @NotBlank
    @Size(min = 6, max = 2000)
    private String content;

    public CreateQuestionForm(String title, String content, String tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }
}
