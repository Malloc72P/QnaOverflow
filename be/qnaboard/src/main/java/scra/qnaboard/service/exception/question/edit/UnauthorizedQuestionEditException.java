package scra.qnaboard.service.exception.question.edit;

public class UnauthorizedQuestionEditException extends QuestionEditFailedException {
    private static final String UNAUTHORIZED = "질문게시글을 수정할 권한이 없습니다. 질문게시글의 작성자가 아니거나 관리자가 아닙니다";

    public UnauthorizedQuestionEditException(long questionId, long requesterId) {
        super(UNAUTHORIZED + " questionId : " + questionId + " requesterId : " + requesterId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-desc-edit-failed-question-unauthorized";
    }
}
