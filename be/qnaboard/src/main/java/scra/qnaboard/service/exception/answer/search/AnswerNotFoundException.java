package scra.qnaboard.service.exception.answer.search;

import scra.qnaboard.service.exception.EntityNotFoundException;

public class AnswerNotFoundException extends EntityNotFoundException {
    private static final String NOT_FOUND = "답변게시글을 찾을 수 없습니다 : ";

    public AnswerNotFoundException(Long questionId) {
        super(NOT_FOUND + questionId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-reason-answer-not-found";
    }
}
