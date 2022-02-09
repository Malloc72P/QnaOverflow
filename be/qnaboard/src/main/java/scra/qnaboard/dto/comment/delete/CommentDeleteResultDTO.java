package scra.qnaboard.dto.comment.delete;

import lombok.Builder;
import lombok.Getter;

/**
 * 댓글 삭제결과에  해당하는 DTO.
 * 댓글이 삭제되면 '삭제된 댓글입니다' 라고 화면에 보여줘야 함.
 * 위의 기능을 구현하기 위해 해당 DTO를 사용한다
 * '삭제된 댓글입니다'라는 문자열은 메세지 소스에서 꺼내와서 사용한다.
 */
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
