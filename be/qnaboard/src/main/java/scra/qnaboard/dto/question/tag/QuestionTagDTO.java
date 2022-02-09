package scra.qnaboard.dto.question.tag;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 질문 목록조회를 위한 DTO. 질문의 태그를 위한 DTO이다.
 * questionId를 DTO에 담는 이유는 QuestionSummaryDTO때문이다.
 * questionId를 담은 컬렉션으로 디비에서 태그를 한번에 다 가져온 다음, GroupingBy로 묶어서 적절한 QuestionDTO에 넣어줘야한다.
 * 뷰에서는 questionId가 필요없지만 in절을 사용하기 위해 넣은 것이다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionTagDTO {
    private long tagId;
    private long questionId;
    private String name;

    @QueryProjection
    public QuestionTagDTO(long tagId, long questionId, String name) {
        this.tagId = tagId;
        this.questionId = questionId;
        this.name = name;
    }
}
