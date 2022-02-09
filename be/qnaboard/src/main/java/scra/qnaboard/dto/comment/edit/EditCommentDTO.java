package scra.qnaboard.dto.comment.edit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditCommentDTO {
    private String content;

    public EditCommentDTO(String content) {
        this.content = content;
    }
}
