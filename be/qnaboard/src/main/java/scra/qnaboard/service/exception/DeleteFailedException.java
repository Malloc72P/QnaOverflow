package scra.qnaboard.service.exception;

public abstract class DeleteFailedException extends RuntimeException implements DescribableException {

    public DeleteFailedException(String message) {
        super(message);
    }
}
