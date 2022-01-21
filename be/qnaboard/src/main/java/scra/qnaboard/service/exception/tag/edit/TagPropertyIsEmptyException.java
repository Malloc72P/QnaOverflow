package scra.qnaboard.service.exception.tag.edit;

public class TagPropertyIsEmptyException extends TagEditFailedException {
    private static final String PROPERTY_IS_EMPTY = "값을 빈 문자열로 변경하면 안됩니다";

    public TagPropertyIsEmptyException(String name, String description) {
        super(PROPERTY_IS_EMPTY + " name : " + name + " description : " + description);
    }
}
