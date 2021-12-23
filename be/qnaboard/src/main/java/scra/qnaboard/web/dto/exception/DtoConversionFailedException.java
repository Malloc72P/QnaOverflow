package scra.qnaboard.web.dto.exception;

public class DtoConversionFailedException extends RuntimeException {
    public static final String ENTITY_OR_FIELD_IS_NULL = "엔티티 또는 필드값이 null이어서 DTO 변환에 실패함";

    public DtoConversionFailedException(String message) {
        super(message);
    }
}
