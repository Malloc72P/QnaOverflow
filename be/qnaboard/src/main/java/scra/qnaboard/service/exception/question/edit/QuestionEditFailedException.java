package scra.qnaboard.service.exception.question.edit;

import scra.qnaboard.service.exception.EditFailedException;

public abstract class QuestionEditFailedException extends EditFailedException {

    protected QuestionEditFailedException(String message) {
        super(message);
    }
}
