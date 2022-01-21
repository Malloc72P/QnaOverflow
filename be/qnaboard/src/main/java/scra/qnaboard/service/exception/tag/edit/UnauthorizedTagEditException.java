package scra.qnaboard.service.exception.tag.edit;

public class UnauthorizedTagEditException extends TagEditFailedException {
    private static final String UNAUTHORIZED = "태그를 수정할 권한이 없습니다. 관리자가 아닙니다";

    public UnauthorizedTagEditException(long answerId, long requesterId) {
        super(UNAUTHORIZED + " answerId : " + answerId + " requesterId : " + requesterId);
    }
}
