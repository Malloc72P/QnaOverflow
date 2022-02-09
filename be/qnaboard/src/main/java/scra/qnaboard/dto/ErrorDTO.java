package scra.qnaboard.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 에러를 담은 DTO.
 * APIErrorDTO와 같은 일을 한다.(사용자에게 왜 요청이 실패했는지를 보여줌)
 * 타임리프 뷰에서 해당 DTO를 받아서 에러페이지를 구현한다.
 */
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
