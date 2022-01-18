package scra.qnaboard.configuration.auth;

public class NoSessionUserException extends RuntimeException {
    private static String MESSAGE = "세션조회 실패! 로그인되지 않은 요청입니다.";

    public NoSessionUserException() {
        super(MESSAGE);
    }
}
