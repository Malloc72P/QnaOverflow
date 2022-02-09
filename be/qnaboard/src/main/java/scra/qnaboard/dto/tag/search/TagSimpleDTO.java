package scra.qnaboard.dto.tag.search;

import lombok.Getter;
import scra.qnaboard.domain.entity.Tag;

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
