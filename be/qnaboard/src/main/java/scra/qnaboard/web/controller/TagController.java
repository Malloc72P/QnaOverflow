package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scra.qnaboard.service.TagService;
import scra.qnaboard.web.dto.tag.create.CreateTagForm;
import scra.qnaboard.web.dto.tag.list.TagListDTO;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final MessageSource message;

    @GetMapping
    public String list(Model model) {
        TagListDTO tagListDTO = tagService.tagList();

        model.addAttribute("tagListDTO", tagListDTO);
        return "/tag/tag-list";
    }

    @GetMapping("form")
    public String tagForm(@ModelAttribute("tagForm") CreateTagForm form) {
        return "/tag/tag-form";
    }

    @PostMapping
    public String create(@ModelAttribute("tagForm") @Validated CreateTagForm form,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Locale locale) {
        if (bindingResult.hasErrors()) {
            return "/tags/form";
        }

        tagService.createTag(1L, form.getName(), form.getDescription());

        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.tag.create.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.tag.create.content", null, locale));

        return "redirect:/notify";
    }


    @PostMapping("/{tagId}/delete")
    public String delete(@PathVariable("tagId") long tagId,
                         RedirectAttributes redirectAttributes,
                         Locale locale) {
        tagService.deleteTag(1L, tagId);

        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.tag.delete.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.tag.delete.content", null, locale));

        return "redirect:/notify";
    }
}
