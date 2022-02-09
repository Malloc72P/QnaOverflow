package scra.qnaboard.dto.question.list;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import scra.qnaboard.dto.exception.DtoUpdateException;
import scra.qnaboard.dto.question.tag.QuestionTagDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static scra.qnaboard.utils.DateTimeUtil.MY_FORMAT;

/**
 * 질문 목록조회를 위한 DTO. 질문목록의 요약정보라고 생각하면 된다. <br>
 * question-item-summary 프래그먼트로 해당 DTO를 넘겨서 뷰를 랜더링한다 <br>
 * createDate를 원하는 포맷으로 뷰에 출력하기 위해 Formatter를 사용한다(@DateTimeFormat)
 */
@Getter
@NoArgsConstructor
public class QuestionSummaryDTO {

    private long questionId;
    private String title;
    private long voteScore;
    private int answerCount;
    private long viewCount;
    @DateTimeFormat(pattern = MY_FORMAT)
    private LocalDateTime createDate;
    private String authorName;
    private List<QuestionTagDTO> tags = new ArrayList<>();

    /**
     * QueryDSL에서 프로젝션할 때 사용하는 생성자
     * 이 메서드는 Q 클래스에서 사용한다. 지우면 안된다!
     */
    @Builder
    @QueryProjection
    public QuestionSummaryDTO(long questionId,
                              String title,
                              int answerCount,
                              long viewCount,
                              long voteScore,
                              LocalDateTime createDate,
                              String authorName) {
        this.questionId = questionId;
        this.title = title;
        this.answerCount = answerCount;
        this.viewCount = viewCount;
        this.voteScore = voteScore;
        this.createDate = createDate;
        this.authorName = authorName;
    }

    public void update(Map<Long, List<QuestionTagDTO>> questionTagMap) {
        if (questionTagMap == null) {
            throw new DtoUpdateException();
        }
        tags = questionTagMap.get(questionId);
    }
}
