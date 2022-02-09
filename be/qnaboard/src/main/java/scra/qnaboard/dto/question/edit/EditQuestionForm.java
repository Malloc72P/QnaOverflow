package scra.qnaboard.dto.question.edit;

import lombok.Getter;
import lombok.ToString;
import scra.qnaboard.dto.question.QuestionTagForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
public class EditQuestionForm extends QuestionTagForm {
    @NotBlank
    @Size(min = 6, max = 200)
    private String title;

    @NotBlank
    @Size(min = 6, max = 2000)
    private String content;

    public EditQuestionForm(String title, String content, String tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
