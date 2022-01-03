package scra.qnaboard.web.dto.comment.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDeleteResultDTO {
    private String deletedAuthorName;
    private String deletedContentName;
}
