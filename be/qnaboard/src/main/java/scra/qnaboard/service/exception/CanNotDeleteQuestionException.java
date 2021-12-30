package scra.qnaboard.service.exception;

public class CanNotDeleteQuestionException extends RuntimeException {
    private static final String NOT_AUTHORIZED = "질문게시글을 삭제할 권한이 없습니다. 질문게시글의 작성자가 아니거나 관리자가 아닙니다";

    public CanNotDeleteQuestionException(Long questionId, Long requesterId) {
        super(NOT_AUTHORIZED + " questionId : " + questionId + " requesterId : " + requesterId);
    }
}
