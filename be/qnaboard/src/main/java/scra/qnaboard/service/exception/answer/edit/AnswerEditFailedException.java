package scra.qnaboard.service.exception.answer.edit;

import scra.qnaboard.service.exception.EditFailedException;

public abstract class AnswerEditFailedException extends EditFailedException {

    protected AnswerEditFailedException(String message) {
        super(message);
    }
}
