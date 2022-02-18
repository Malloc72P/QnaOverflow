package scra.qnaboard.service.exception.answer.edit;

public class AnswerPropertyIsEmptyException extends AnswerEditFailedException {
    private static final String PROPERTY_IS_EMPTY = "값을 빈 문자열로 변경하면 안됩니다";

    public AnswerPropertyIsEmptyException(String content) {
        super(PROPERTY_IS_EMPTY + " content : " + content);
    }

    @Override
    public String describeMessage() {
        return "ui.error.page-desc-edit-failed-answer-property-empty";
    }
}
