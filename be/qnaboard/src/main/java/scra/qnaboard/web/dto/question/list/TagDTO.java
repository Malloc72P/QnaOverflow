package scra.qnaboard.web.dto.question.list;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import scra.qnaboard.domain.entity.Tag;

/**
 * 질문 목록조회를 위한 DTO. 질문의 태그를 위한 DTO이다.
 */
@Getter
public class TagDTO {
    private long tagId;
    private long questionId;
    private String name;

    @QueryProjection
    public TagDTO(long tagId, long questionId, String name) {
        this.tagId = tagId;
        this.questionId = questionId;
        this.name = name;
    }

    public static TagDTO from(Tag tag) {
        return new TagDTO(tag.getId(), 0L, tag.getName());
    }
}
