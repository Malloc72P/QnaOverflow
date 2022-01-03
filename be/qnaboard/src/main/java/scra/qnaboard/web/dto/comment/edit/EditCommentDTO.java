package scra.qnaboard.web.dto.comment.edit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditCommentDTO {
    private String content;
}
