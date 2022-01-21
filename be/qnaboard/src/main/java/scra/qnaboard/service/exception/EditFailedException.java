package scra.qnaboard.service.exception;

public abstract class EditFailedException extends RuntimeException implements DescriptionMessageCodeSupplier {
    public EditFailedException(String message) {
        super(message);
    }
}
