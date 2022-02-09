package scra.qnaboard.dto.exception;

/**
 * 엔티티 -> DTO 변환에 실패하는 경우를 위한 예외 <br>
 * DTO의 from 정적메서드에서 해당 예외를 발생시킬 수 있다.
 */
public class DtoConversionFailedException extends RuntimeException {
    public static final String ENTITY_OR_FIELD_IS_NULL = "엔티티 또는 필드값이 null이어서 DTO 변환에 실패함";

    public DtoConversionFailedException(String message) {
        super(message);
    }
}
