package scra.qnaboard.web.exception.member;

public class NotLoggedInException extends RuntimeException {
    public static final String CAN_NOT_LOGOUT = "로그아웃할 수 없습니다. 로그인 된 상태가 아닙니다";

    public NotLoggedInException(String message) {
        super(message);
    }
}
