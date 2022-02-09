package scra.qnaboard.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 알림페이지를 위한 DTO.
 */
@Getter
@AllArgsConstructor
public class NotifyDTO {

    private String title;
    private String content;
}
