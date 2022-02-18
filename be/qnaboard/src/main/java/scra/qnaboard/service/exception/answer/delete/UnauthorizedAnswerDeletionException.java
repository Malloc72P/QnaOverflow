package scra.qnaboard.service.exception.answer.delete;

import scra.qnaboard.service.exception.DeleteFailedException;

public class UnauthorizedAnswerDeletionException extends DeleteFailedException {
    public static final String MESSAGE = "답변게시글을 삭제할 권한이 없습니다. 답변게시글의 작성자가 아니거나 관리자가 아닙니다";

    public UnauthorizedAnswerDeletionException(Long questionId, Long requesterId) {
        super(MESSAGE + " questionId : " + questionId + " requesterId : " + requesterId);
    }

    @Override
    public String describeMessage() {
        return "ui.error.page-desc-delete-failed-answer";
    }
}
