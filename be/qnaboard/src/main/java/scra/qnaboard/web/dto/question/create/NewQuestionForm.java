package scra.qnaboard.web.dto.question.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@ToString
public class NewQuestionForm {

    @NotBlank
    @Size(min = 6, max = 300)
    private String title;

    @NotBlank
    @Size(min = 6)
    private String content;
}
