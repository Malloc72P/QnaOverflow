package scra.qnaboard.dto.comment.edit;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 댓글 수정결과에 해당하는 DTO.
 * 서비스에서 반환함.
 */
@Getter
@AllArgsConstructor
public class EditCommentResultDTO {

    private String content;

}
