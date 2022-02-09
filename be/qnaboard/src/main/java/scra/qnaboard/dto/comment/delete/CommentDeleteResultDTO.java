package scra.qnaboard.dto.comment.delete;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDeleteResultDTO {
    private String deletedAuthorName;
    private String deletedContentName;

    @Builder
    public CommentDeleteResultDTO(String deletedAuthorName, String deletedContentName) {
        this.deletedAuthorName = deletedAuthorName;
        this.deletedContentName = deletedContentName;
    }
}
