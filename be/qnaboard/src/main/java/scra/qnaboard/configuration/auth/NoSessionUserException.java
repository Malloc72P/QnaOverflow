package scra.qnaboard.configuration.auth;

import scra.qnaboard.service.exception.DescriptionMessageCodeSupplier;

public class NoSessionUserException extends RuntimeException implements DescriptionMessageCodeSupplier {
    private static final String MESSAGE = "세션조회 실패! 로그인되지 않은 요청입니다.";

    public NoSessionUserException() {
        super(MESSAGE);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-desc-no-log-in";
    }
}
