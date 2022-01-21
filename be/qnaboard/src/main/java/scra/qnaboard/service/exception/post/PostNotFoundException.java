package scra.qnaboard.service.exception.post;

import scra.qnaboard.service.exception.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {
    private static final String ENTITY_NOT_FOUND = "게시글을 찾을 수 없습니다 : ";

    public PostNotFoundException(Long postId) {
        super(ENTITY_NOT_FOUND + postId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-reason-post-not-found";
    }

}
