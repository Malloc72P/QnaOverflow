package scra.qnaboard.service.exception.answer.delete;

public class AnswerDeleteFailedException extends RuntimeException {
    public static final String UNAUTHORIZED = "답변게시글을 삭제할 권한이 없습니다. 답변게시글의 작성자가 아니거나 관리자가 아닙니다";

    public AnswerDeleteFailedException(String message, Long questionId, Long requesterId) {
        super(UNAUTHORIZED + " questionId : " + questionId + " requesterId : " + requesterId);
    }
}
