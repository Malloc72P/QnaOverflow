package scra.qnaboard.service.exception;

public abstract class DeleteFailedException extends RuntimeException implements DescriptionMessageCodeSupplier{

    public DeleteFailedException(String message) {
        super(message);
    }
}
