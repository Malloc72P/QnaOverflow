package scra.qnaboard.service.exception.comment;

import scra.qnaboard.service.exception.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_NOT_FOUND = "댓글을 찾을 수 없습니다 : ";

    public CommentNotFoundException(Long postId) {
        super(ENTITY_NOT_FOUND + postId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-reason-comment-not-found";
    }
}
