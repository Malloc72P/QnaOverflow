package scra.qnaboard.service.exception.question.edit;

public class QuestionPropertyIsEmptyException extends QuestionEditFailedException {
    private static final String MESSAGE = "값을 빈 문자열로 변경하면 안됩니다";

    public QuestionPropertyIsEmptyException(String title, String content) {
        super(MESSAGE + " title : " + title + " content : " + content);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-desc-edit-failed-question-property-empty";
    }
}
