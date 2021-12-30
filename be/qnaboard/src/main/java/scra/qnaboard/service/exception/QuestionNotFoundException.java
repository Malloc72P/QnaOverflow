package scra.qnaboard.service.exception;

public class QuestionNotFoundException extends RuntimeException {
    private static final String NOT_FOUND = "질문게시글을 찾을 수 없습니다 : ";

    public QuestionNotFoundException(Long questionId) {
        super(NOT_FOUND + questionId);
    }
}
