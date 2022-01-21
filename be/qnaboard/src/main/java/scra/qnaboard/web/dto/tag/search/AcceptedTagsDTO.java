package scra.qnaboard.web.dto.tag.search;

import lombok.Getter;
import scra.qnaboard.domain.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AcceptedTagsDTO {

    private List<TagSimpleDTO> tags = new ArrayList<>();

    public static AcceptedTagsDTO from(List<Tag> tags) {
        AcceptedTagsDTO dto = new AcceptedTagsDTO();

        dto.tags = tags.stream()
                .map(TagSimpleDTO::from)
                .collect(Collectors.toList());

        return dto;
    }
}
