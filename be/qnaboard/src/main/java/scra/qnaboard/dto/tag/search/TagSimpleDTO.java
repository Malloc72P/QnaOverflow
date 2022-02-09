package scra.qnaboard.dto.tag.search;

import lombok.Getter;
import scra.qnaboard.domain.entity.Tag;

/**
 * 태그 목록조회에 사용하는 DTO.
 * TagDTO와 비슷하지만, 이쪽은 작성자와 같은 정보가 빠져있다.
 */
@Getter
public class TagSimpleDTO {

    private long id;
    private String name;
    private String description;

    public static TagSimpleDTO from(Tag tag) {
        TagSimpleDTO tagSimpleDTO = new TagSimpleDTO();

        tagSimpleDTO.id = tag.getId();
        tagSimpleDTO.name = tag.getName();
        tagSimpleDTO.description = tag.getDescription();

        return tagSimpleDTO;
    }
}
