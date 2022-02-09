package scra.qnaboard.dto.answer.edit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import scra.qnaboard.web.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;

/**
 * 답변글 수정요청의 처리 결과에 해당하는 DTO
 * 서비스에서 반환하게 됨.
 * 새로 수정된 답변글의 내용과 최근 수정일을 담아서 반환함
 */
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
