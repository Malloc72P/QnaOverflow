package scra.qnaboard.service.exception.comment.edit;

public class CommentPropertyIsEmptyException extends CommentEditFailedException {
    private static final String PROPERTY_IS_EMPTY = "값을 빈 문자열로 변경하면 안됩니다";

    public CommentPropertyIsEmptyException(String content) {
        super(PROPERTY_IS_EMPTY + " content : " + content);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-desc-edit-failed-comment-property-empty";
    }
}
