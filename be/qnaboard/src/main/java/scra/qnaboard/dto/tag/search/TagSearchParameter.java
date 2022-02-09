package scra.qnaboard.dto.tag.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * 태그 검색 API에서 사용하는 DTO.
 * 컨트롤러의 파라미터에서 사용한다
 */
@Getter
@AllArgsConstructor
public class TagSearchParameter {

    @NotBlank
    private String keyword;
}
