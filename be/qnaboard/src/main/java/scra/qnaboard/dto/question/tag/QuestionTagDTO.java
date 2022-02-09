package scra.qnaboard.dto.question.tag;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 질문글 목록, 상세조회를 위한 태그 DTO.
 * DTO에 questionId가 있는 이유는 질문글 목록조회기능의 쿼리최적화 때문이다.
 * 질문글 아이디 리스트와 쿼리의 In절을 활용해서 태그정보를 디비에서 한번에 다 가져오기 때문이다.
 * 뷰에서 questionId를 사용하지 않는다고 지우면 안된다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionTagDTO {
    private long tagId;
    private long questionId;
    private String name;//태그 이름

    @QueryProjection
    public QuestionTagDTO(long tagId, long questionId, String name) {
        this.tagId = tagId;
        this.questionId = questionId;
        this.name = name;
    }
}
