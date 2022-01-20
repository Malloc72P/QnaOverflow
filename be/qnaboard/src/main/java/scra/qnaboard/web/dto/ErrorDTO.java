package scra.qnaboard.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorDTO {
    private int status;
    private String title;
    private String reason;
    private String description;

    @Builder
    public ErrorDTO(int status, String title, String reason, String description) {
        this.status = status;
        this.title = title;
        this.reason = reason;
        this.description = description;
    }
}
