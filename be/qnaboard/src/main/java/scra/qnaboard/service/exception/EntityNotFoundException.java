package scra.qnaboard.service.exception;

public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public abstract String descriptionMessageCode();
}
