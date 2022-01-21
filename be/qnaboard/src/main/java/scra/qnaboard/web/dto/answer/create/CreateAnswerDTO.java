package scra.qnaboard.web.dto.answer.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAnswerDTO {

    @NotBlank
    @Size(min = 6, max = 65536, message = "{Size.createAnswerDTO.content}")
    private String content;
}