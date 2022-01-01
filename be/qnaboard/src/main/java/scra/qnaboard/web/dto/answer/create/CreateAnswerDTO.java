package scra.qnaboard.web.dto.answer.create;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAnswerDTO {

    @NotBlank
    @Size(min = 6)
    private String content;
}
