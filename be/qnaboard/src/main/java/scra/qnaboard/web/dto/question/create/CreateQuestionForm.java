package scra.qnaboard.web.dto.question.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import scra.qnaboard.web.dto.question.QuestionTagForm;
import scra.qnaboard.web.exception.question.create.ExtractingTagIdFailedException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class CreateQuestionForm extends QuestionTagForm {

    @NotBlank
    @Size(min = 6, max = 300)
    private String title;

    @NotBlank
    @Size(min = 6)
    private String content;

    public CreateQuestionForm(String title, String content, String tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }
}
