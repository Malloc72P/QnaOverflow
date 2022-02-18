package scra.qnaboard.configuration.auth;

import scra.qnaboard.service.exception.DescribableException;

/**
 * 세션에서 유저DTO를 찾지 못한 경우 발생하는 예외.
 * 로그인되지 않은 상태에서 로그인이 필요한 작업을 요청하는 경우 해당 예외가 발생된다
 */
public class NoSessionUserException extends RuntimeException implements DescribableException {
    private static final String MESSAGE = "세션조회 실패! 로그인되지 않은 요청입니다.";

    public NoSessionUserException() {
        super(MESSAGE);
    }

    @Override
    public String describeMessage() {
        return "ui.error.page-desc-no-log-in";
    }
}
