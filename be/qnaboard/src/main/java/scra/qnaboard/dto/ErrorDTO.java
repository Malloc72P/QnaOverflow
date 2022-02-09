package scra.qnaboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorDTO {
    private String title;
    private String reason;
    private String description;

    @Builder
    public ErrorDTO(String title, String reason, String description) {
        this.title = title;
        this.reason = reason;
        this.description = description;
    }
}
