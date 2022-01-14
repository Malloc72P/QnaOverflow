package scra.qnaboard.service.exception.question;

public class AlreadyDeletedQuestionException extends RuntimeException {
    private static final String ALREADY_DELETED = "이미 삭제된 질문글입니다";

    public AlreadyDeletedQuestionException() {
        super(ALREADY_DELETED);
    }
}
