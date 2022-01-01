package scra.qnaboard.web.dto.answer.edit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditAnswerDTO {

    private String content;
}
