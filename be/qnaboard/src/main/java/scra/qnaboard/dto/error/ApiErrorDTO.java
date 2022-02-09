package scra.qnaboard.dto.error;

import lombok.Builder;
import lombok.Getter;

/**
 * API 에러를 담은 DTO.
 * API 요청 처리에 실패한 경우, 이 DTO를 만들어서 사용자에게 반환한다.
 * 사용자는 해당 DTO에 담긴 내용을 화면을 통해 보게 되고, 왜 요청이 실패했는지를 알 수 있다.
 */
@Getter
public class ApiErrorDTO {
    private final int status;
    private final String description;

    @Builder
    public ApiErrorDTO(int status, String description) {
        this.status = status;
        this.description = description;
    }
}
