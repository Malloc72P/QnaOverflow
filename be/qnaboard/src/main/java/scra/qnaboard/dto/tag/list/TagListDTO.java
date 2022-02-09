package scra.qnaboard.dto.tag.list;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class TagListDTO {
    List<TagDTO> tags = new ArrayList<>();
}
