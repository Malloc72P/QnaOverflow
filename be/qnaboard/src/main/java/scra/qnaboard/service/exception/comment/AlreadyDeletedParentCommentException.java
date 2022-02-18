package scra.qnaboard.service.exception.comment;

import scra.qnaboard.service.exception.EntityNotFoundException;

public class AlreadyDeletedParentCommentException extends EntityNotFoundException {
    private static final String MESSAGE = "댓글의 부모댓글이 지워짐 : ";

    public AlreadyDeletedParentCommentException(Long commentId) {
        super(MESSAGE + commentId);
    }

    @Override
    public String describeMessage() {
        return "ui.error.page-reason-comment-parent-already-deleted";
    }
}
