package scra.qnaboard.web.dto.tag.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class TagSearchParameter {

    @NotBlank
    private String keyword;
}
