package scra.qnaboard.dto.tag.list;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.utils.DateTimeUtil;

import java.time.LocalDateTime;

/**
 * 태그 목록조회를 위한 DTO
 */
@Getter
@NoArgsConstructor
public class TagDTO {

    private long tagId;
    private long authorId;
    private String authorName;
    private String tagName;
    private String tagDescription;
    @DateTimeFormat(pattern = DateTimeUtil.MY_FORMAT)
    private LocalDateTime createdDate;

    @Builder
    public TagDTO(long tagId, long authorId, String authorName, String tagName, String tagDescription, LocalDateTime createdDate) {
        this.tagId = tagId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.tagName = tagName;
        this.tagDescription = tagDescription;
        this.createdDate = createdDate;
    }

    /**
     * 엔티티에서 DTO로 변환함
     */
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
