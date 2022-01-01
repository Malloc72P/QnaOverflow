package scra.qnaboard.service.exception.answer.search;

public class AnswerNotFoundException extends RuntimeException {
    private static final String NOT_FOUND = "답변게시글을 찾을 수 없습니다 : ";

    public AnswerNotFoundException(Long questionId) {
        super(NOT_FOUND + questionId);
    }
}
