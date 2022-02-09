package scra.qnaboard.dto.question.edit;

import lombok.Getter;
import lombok.ToString;
import scra.qnaboard.dto.question.QuestionTagForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 질문글 수정을 위한 DTO
 * 컨트롤러의 파라미터에서 사용함.
 * 질문글 수정 페이지의 내용을 채우기 위해서 사용한다.
 * 수정에 실패한 경우, 왜 실패했는지 사용자에게 알려주기 위해서 사용하기도 함
 */
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
