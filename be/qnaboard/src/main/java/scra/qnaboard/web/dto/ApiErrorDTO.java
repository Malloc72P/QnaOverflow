package scra.qnaboard.web.dto;

import lombok.Builder;
import lombok.Getter;

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
