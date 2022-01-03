package scra.qnaboard.web.dto.comment.create;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCommentDTO {
    private Long parentCommentId;
    private String content;
}
