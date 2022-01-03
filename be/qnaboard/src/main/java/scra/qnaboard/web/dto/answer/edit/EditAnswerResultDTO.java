package scra.qnaboard.web.dto.answer.edit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import scra.qnaboard.web.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EditAnswerResultDTO {

    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = LocalDateTimeUtils.STRING_DATE_TIME_FORMAT)
    private LocalDateTime lastModifiedDate;
}
