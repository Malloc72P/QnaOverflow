package scra.qnaboard.service.exception.tag.delete;

public class TagDeleteFailedException extends RuntimeException {
    public static final String UNAUTHORIZED = "태그를 삭제할 권한이 없습니다. 관리자가 아닙니다";

    public TagDeleteFailedException(String message, Long tagId, Long requesterId) {
        super(UNAUTHORIZED + " tagId : " + tagId + " requesterId : " + requesterId);
    }
}
