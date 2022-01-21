package scra.qnaboard.web.dto.answer.edit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditAnswerDTO {

    @NotBlank
    @Size(min = 6, max = 65536, message = "{Size.editAnswerDTO.content}")
    private String content;
}
