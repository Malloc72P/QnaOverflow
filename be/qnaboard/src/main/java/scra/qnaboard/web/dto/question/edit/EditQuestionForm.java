package scra.qnaboard.web.dto.question.edit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
@AllArgsConstructor
public class EditQuestionForm {
    @NotBlank
    @Size(min = 6, max = 300)
    private String title;

    @NotBlank
    @Size(min = 6)
    private String content;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
