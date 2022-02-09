package scra.qnaboard.dto.comment.edit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 수정요청에 해당하는 DTO.
 * 컨트롤러의 파라미터에서 사용함
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditCommentDTO {
    private String content;

    public EditCommentDTO(String content) {
        this.content = content;
    }
}
