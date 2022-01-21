package scra.qnaboard.service.exception.tag.delete;

import scra.qnaboard.service.exception.DeleteFailedException;

public class UnauthorizedTagDeletionException extends DeleteFailedException {
    public static final String MESSAGE = "태그를 삭제할 권한이 없습니다. 관리자가 아닙니다";

    public UnauthorizedTagDeletionException(Long tagId, Long requesterId) {
        super(MESSAGE + " tagId : " + tagId + " requesterId : " + requesterId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-desc-delete-failed-tag";
    }
}
