package scra.qnaboard.service.exception.answer.edit;

public class ForbiddenAnswerEditException extends AnswerEditFailedException {
    private static final String UNAUTHORIZED = "답변게시글을 수정할 권한이 없습니다. 답변게시글의 작성자가 아니거나 관리자가 아닙니다";

    public ForbiddenAnswerEditException(long answerId, long requesterId) {
        super(UNAUTHORIZED + " answerId : " + answerId + " requesterId : " + requesterId);
    }

    @Override
    public String describeMessage() {
        return "ui.error.page-desc-edit-failed-answer-unauthorized";
    }
}
