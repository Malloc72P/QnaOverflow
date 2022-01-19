package scra.qnaboard.service.exception.member;

import scra.qnaboard.service.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {
    private static final String NOT_FOUND = "멤버를 찾을 수 없습니다 : ";

    public MemberNotFoundException(Long questionId) {
        super(NOT_FOUND + questionId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-reason-member-not-found";
    }
}
