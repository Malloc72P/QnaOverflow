package scra.qnaboard.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scra.qnaboard.dto.tag.search.TagSearchParameter;
import scra.qnaboard.dto.tag.search.TagSearchResultDTO;
import scra.qnaboard.service.TagService;

/**
 * 태그 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagApiController {

    private final TagService tagService;

    /**
     * 태그 검색요청을 처리하는 핸들러
     *
     * @param searchParameter 태그 검색 파라미터. 이걸로 태그를 검색한다.
     * @return 검색된 태그 목록(JSON)
     */
    @GetMapping
    public TagSearchResultDTO search(
            @ModelAttribute @Validated TagSearchParameter searchParameter) {
        return tagService.search(searchParameter.getKeyword());
    }

}
