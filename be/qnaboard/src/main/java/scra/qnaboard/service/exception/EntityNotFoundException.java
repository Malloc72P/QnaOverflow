package scra.qnaboard.service.exception;

public abstract class EntityNotFoundException extends RuntimeException implements DescriptionMessageCodeSupplier{

    public EntityNotFoundException(String message) {
        super(message);
    }

}
