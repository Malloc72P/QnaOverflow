package scra.qnaboard.web.dto.tag.list;

import lombok.Getter;
import scra.qnaboard.domain.entity.Tag;

import java.time.LocalDateTime;

@Getter
public class TagDTO {

    private long id;
    private long authorId;
    private String authorName;
    private String tagName;
    private String tagDescription;
    private LocalDateTime createdDate;

    public static TagDTO from(Tag tag) {
        TagDTO tagDTO = new TagDTO();

        tagDTO.id = tag.getId();
        tagDTO.authorId = tag.getAuthor().getId();
        tagDTO.authorName = tag.getAuthor().getNickname();
        tagDTO.tagName = tag.getName();
        tagDTO.tagDescription = tag.getDescription();
        tagDTO.createdDate = tag.getCreatedDate();

        return tagDTO;
    }
}
