package scra.qnaboard.service.exception.answer.edit;

public class UnauthorizedAnswerEditException extends AnswerEditFailedException {
    private static final String UNAUTHORIZED = "답변게시글을 수정할 권한이 없습니다. 답변게시글의 작성자가 아니거나 관리자가 아닙니다";

    public UnauthorizedAnswerEditException(long answerId, long requesterId) {
        super(UNAUTHORIZED + " answerId : " + answerId + " requesterId : " + requesterId);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-desc-edit-failed-answer-unauthorized";
    }
}
