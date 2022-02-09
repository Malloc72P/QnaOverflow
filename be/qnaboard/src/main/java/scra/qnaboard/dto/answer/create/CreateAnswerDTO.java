package scra.qnaboard.dto.answer.create;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAnswerDTO {

    @NotBlank
    @Size(min = 6, max = 2000, message = "{Size.createAnswerDTO.content}")
    private String content;
}
