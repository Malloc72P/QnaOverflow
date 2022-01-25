package scra.qnaboard.service.exception.comment;

import scra.qnaboard.service.exception.EntityNotFoundException;

public class AlreadyDeletedCommentException extends EntityNotFoundException {
    private static final String MESSAGE = "이미 삭제된 댓글입니다 : ";

    public AlreadyDeletedCommentException(Long commentId) {
        super(MESSAGE + commentId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-reason-comment-already-deleted";
    }
}
