package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import scra.qnaboard.service.TagService;
import scra.qnaboard.web.dto.tag.list.TagDTO;
import scra.qnaboard.web.dto.tag.list.TagListDTO;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public String list(Model model) {
        TagListDTO tagListDTO = tagService.list();

        model.addAttribute("tagListDTO", tagListDTO);
        return "/tag/tag-list";
    }
}
