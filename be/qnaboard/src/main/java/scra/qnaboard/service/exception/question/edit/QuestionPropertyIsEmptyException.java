package scra.qnaboard.service.exception.question.edit;

public class QuestionPropertyIsEmptyException extends QuestionEditFailedException {
    private static final String PROPERTY_IS_EMPTY = "값을 빈 문자열로 변경하면 안됩니다";

    public QuestionPropertyIsEmptyException(String title, String content) {
        super(PROPERTY_IS_EMPTY + " title : " + title + " content : " + content);
    }
}
