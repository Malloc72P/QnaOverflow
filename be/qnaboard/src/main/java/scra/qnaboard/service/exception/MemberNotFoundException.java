package scra.qnaboard.service.exception;

public class MemberNotFoundException extends RuntimeException {
    private static final String NOT_FOUND = "멤버를 찾을 수 없습니다 : ";

    public MemberNotFoundException(Long questionId) {
        super(NOT_FOUND + questionId);
    }
}
