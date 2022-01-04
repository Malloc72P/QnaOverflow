package scra.qnaboard.web.dto.tag.list;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.utils.DateTimeUtil;

import java.time.LocalDateTime;

@Getter
public class TagDTO {

    private long tagId;
    private long authorId;
    private String authorName;
    private String tagName;
    private String tagDescription;
    @DateTimeFormat(pattern = DateTimeUtil.MY_FORMAT)
    private LocalDateTime createdDate;

    public static TagDTO from(Tag tag) {
        TagDTO tagDTO = new TagDTO();

        tagDTO.tagId = tag.getId();
        tagDTO.authorId = tag.getAuthor().getId();
        tagDTO.authorName = tag.getAuthor().getNickname();
        tagDTO.tagName = tag.getName();
        tagDTO.tagDescription = tag.getDescription();
        tagDTO.createdDate = tag.getCreatedDate();

        return tagDTO;
    }
}
