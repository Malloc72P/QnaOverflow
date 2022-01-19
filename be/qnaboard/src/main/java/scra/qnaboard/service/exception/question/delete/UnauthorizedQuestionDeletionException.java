package scra.qnaboard.service.exception.question.delete;

import scra.qnaboard.service.exception.DeleteFailedException;

public class UnauthorizedQuestionDeletionException extends DeleteFailedException {
    public static final String MESSAGE = "질문게시글을 삭제할 권한이 없습니다. 질문게시글의 작성자가 아니거나 관리자가 아닙니다";

    public UnauthorizedQuestionDeletionException(Long questionId, Long requesterId) {
        super(MESSAGE + " questionId : " + questionId + " requesterId : " + requesterId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-reason-delete-failed-question";
    }
}
