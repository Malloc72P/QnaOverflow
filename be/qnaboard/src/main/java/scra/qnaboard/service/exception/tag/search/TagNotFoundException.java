package scra.qnaboard.service.exception.tag.search;

public class TagNotFoundException extends RuntimeException {
    private static final String NOT_FOUND = "태그를 찾을 수 없습니다 : ";

    public TagNotFoundException(Long questionId) {
        super(NOT_FOUND + questionId);
    }
}
