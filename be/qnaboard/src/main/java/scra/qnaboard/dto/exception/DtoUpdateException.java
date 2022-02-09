package scra.qnaboard.dto.exception;

public class DtoUpdateException extends RuntimeException {
    private static final String message = "null로 초기화하면 안되는 필드를 null로 초기화하려고 했습니다";

    public DtoUpdateException() {
        super(message);
    }
}
