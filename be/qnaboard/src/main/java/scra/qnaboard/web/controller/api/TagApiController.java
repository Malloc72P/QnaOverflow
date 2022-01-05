package scra.qnaboard.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scra.qnaboard.service.TagService;
import scra.qnaboard.web.dto.tag.search.AcceptedTagsDTO;
import scra.qnaboard.web.dto.tag.search.TagSearchParameter;
import scra.qnaboard.web.dto.tag.search.TagSearchResultDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagApiController {

    private final TagService tagService;

    @GetMapping
    public TagSearchResultDTO search(
            @ModelAttribute @Validated TagSearchParameter searchParameter) {
        return tagService.search(searchParameter.getKeyword());
    }

}
