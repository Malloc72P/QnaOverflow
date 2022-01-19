package scra.qnaboard.service.exception.question.search;

import scra.qnaboard.service.exception.EntityNotFoundException;

public class QuestionNotFoundException extends EntityNotFoundException {
    private static final String NOT_FOUND = "질문게시글을 찾을 수 없습니다 : ";

    public QuestionNotFoundException(Long questionId) {
        super(NOT_FOUND + questionId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-reason-question-not-found";
    }
}
