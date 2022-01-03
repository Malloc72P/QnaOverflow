package scra.qnaboard.service.exception.comment.delete;

public class CommentDeleteFailedException extends RuntimeException {
    public static final String UNAUTHORIZED = "댓글을 삭제할 권한이 없습니다. 댓글게시글의 작성자가 아니거나 관리자가 아닙니다";

    public CommentDeleteFailedException(String message, Long commentId, Long requesterId) {
        super(UNAUTHORIZED + " commentId : " + commentId + " requesterId : " + requesterId);
    }
}
