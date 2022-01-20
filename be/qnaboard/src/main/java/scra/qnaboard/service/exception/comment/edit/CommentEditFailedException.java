package scra.qnaboard.service.exception.comment.edit;

import scra.qnaboard.service.exception.EditFailedException;

public abstract class CommentEditFailedException extends EditFailedException {

    protected CommentEditFailedException(String message) {
        super(message);
    }
}
