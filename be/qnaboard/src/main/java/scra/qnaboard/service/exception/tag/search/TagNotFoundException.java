package scra.qnaboard.service.exception.tag.search;

import scra.qnaboard.service.exception.EntityNotFoundException;

public class TagNotFoundException extends EntityNotFoundException {
    private static final String NOT_FOUND = "태그를 찾을 수 없습니다 : ";

    public TagNotFoundException(Long questionId) {
        super(NOT_FOUND + questionId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-reason-tag-not-found";
    }
}
