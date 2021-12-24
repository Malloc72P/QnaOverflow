package scra.qnaboard.web.dto.question.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import scra.qnaboard.domain.entity.Tag;

/**
 * 질문 목록조회를 위한 DTO. 질문의 태그를 위한 DTO이다.
 */
@Getter
@AllArgsConstructor
public class TagDTO {
    private long id;
    private String name;

    public static TagDTO from(Tag tag) {
        return new TagDTO(tag.getId(), tag.getName());
    }
}
