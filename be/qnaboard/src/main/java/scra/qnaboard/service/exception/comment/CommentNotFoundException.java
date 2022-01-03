package scra.qnaboard.service.exception.comment;

public class CommentNotFoundException extends RuntimeException{
    private static final String ENTITY_NOT_FOUND = "댓글을 찾을 수 없습니다 : ";

    public CommentNotFoundException(Long postId) {
        super(ENTITY_NOT_FOUND + postId);
    }

}
