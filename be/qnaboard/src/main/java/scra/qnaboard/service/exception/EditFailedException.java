package scra.qnaboard.service.exception;

public abstract class EditFailedException extends RuntimeException implements DescribableException {
    public EditFailedException(String message) {
        super(message);
    }
}
