package scra.qnaboard.service.exception;

public abstract class EntityNotFoundException extends RuntimeException implements DescribableException {

    public EntityNotFoundException(String message) {
        super(message);
    }

}
