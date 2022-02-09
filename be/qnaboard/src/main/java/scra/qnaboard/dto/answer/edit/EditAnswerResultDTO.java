package scra.qnaboard.dto.answer.edit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import scra.qnaboard.web.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;

@Getter
public class EditAnswerResultDTO {

    private String content;

    @JsonFormat(pattern = LocalDateTimeUtils.STRING_DATE_TIME_FORMAT)
    private LocalDateTime lastModifiedDate;

    @Builder
    public EditAnswerResultDTO(String content, LocalDateTime lastModifiedDate) {
        this.content = content;
        this.lastModifiedDate = lastModifiedDate;
    }
}
