package scra.qnaboard.dto.answer.create;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 답변글 생성 요청할 때 사용하는 DTO.
 * 컨트롤러의 파라미터에서 사용함.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAnswerDTO {

    @NotBlank
    @Size(min = 6, max = 2000, message = "{Size.createAnswerDTO.content}")
    private String content;
}
