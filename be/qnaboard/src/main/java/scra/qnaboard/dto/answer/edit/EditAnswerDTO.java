package scra.qnaboard.dto.answer.edit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 답변글 수정 요청할 때 사용하는 DTO.
 * 컨트롤러의 파라미터에서 사용함.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditAnswerDTO {

    @NotBlank
    @Size(min = 6, max = 2000, message = "{Size.editAnswerDTO.content}")
    private String content;
}
