package scra.qnaboard.service.exception.post;

public class PostNotFoundException extends RuntimeException {
    private static final String ENTITY_NOT_FOUND = "게시글을 찾을 수 없습니다 : ";

    public PostNotFoundException(Long postId) {
        super(ENTITY_NOT_FOUND + postId);
    }

}
