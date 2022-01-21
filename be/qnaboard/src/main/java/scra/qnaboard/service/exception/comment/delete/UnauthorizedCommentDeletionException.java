package scra.qnaboard.service.exception.comment.delete;

import scra.qnaboard.service.exception.DeleteFailedException;

public class UnauthorizedCommentDeletionException extends DeleteFailedException {
    public static final String MESSAGE = "댓글을 삭제할 권한이 없습니다. 댓글게시글의 작성자가 아니거나 관리자가 아닙니다";

    public UnauthorizedCommentDeletionException(Long commentId, Long requesterId) {
        super(MESSAGE + " commentId : " + commentId + " requesterId : " + requesterId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-desc-delete-failed-comment";
    }
}
