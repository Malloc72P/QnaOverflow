package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.TagService;
import scra.qnaboard.web.dto.page.Paging;
import scra.qnaboard.web.dto.tag.create.CreateTagForm;
import scra.qnaboard.web.dto.tag.edit.EditTagForm;
import scra.qnaboard.web.dto.tag.list.TagDTO;
import scra.qnaboard.web.dto.tag.list.TagListDTO;

import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private static final String defaultPageNumber = "0";
    private static final String defaultPageSize = "12";

    private final TagService tagService;
    private final MessageSource message;

    @GetMapping
    public String list(@RequestParam(defaultValue = defaultPageNumber) int pageNumber,
                       @RequestParam(defaultValue = defaultPageSize) int pageSize,
                       Model model) {
        Page<TagDTO> tagDTOPage = tagService.tagList(pageNumber, pageSize);
        Paging<TagDTO> tagDTOPaging = Paging.buildPaging(tagDTOPage);
        model.addAttribute("paging", tagDTOPaging);
        return "tag/tag-list";
    }

    @GetMapping("form")
    public String tagForm(@ModelAttribute("tagForm") CreateTagForm form) {
        return "tag/tag-form";
    }

    @PostMapping
    public String create(@ModelAttribute("tagForm") @Validated CreateTagForm form,
                         BindingResult bindingResult,
                         @LoginUser SessionUser sessionUser,
                         RedirectAttributes redirectAttributes,
                         Locale locale) {
        if (bindingResult.hasErrors()) {
            return "tag/tag-form";
        }

        tagService.createTag(sessionUser.getId(), form.getName(), form.getDescription());

        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.tag.create.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.tag.create.content", null, locale));

        return "redirect:/notify";
    }

    @GetMapping("{tagId}/edit-form")
    public String tagEditForm(@PathVariable("tagId") long tagId,
                              @ModelAttribute("editTagForm") EditTagForm form,
                              Model model) {
        TagDTO tagDTO = tagService.tagById(tagId);
        form.update(tagDTO.getTagName(), tagDTO.getTagDescription());
        model.addAttribute("tagId", tagId);
        return "tag/tag-edit-form";
    }

    @PostMapping("{tagId}/edit")
    public String edit(@PathVariable("tagId") long tagId,
                       @ModelAttribute("editTagForm") @Validated EditTagForm form,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       @LoginUser SessionUser sessionUser,
                       Locale locale) {
        if (bindingResult.hasErrors()) {
            return "tag/tag-edit-form";
        }

        tagService.editTag(sessionUser.getId(), tagId, form.getName(), form.getDescription());

        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.tag.edit.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.tag.edit.content", null, locale));

        return "redirect:/notify";
    }


    @PostMapping("{tagId}/delete")
    public String delete(@PathVariable("tagId") long tagId,
                         RedirectAttributes redirectAttributes,
                         @LoginUser SessionUser sessionUser,
                         Locale locale) {
        tagService.deleteTag(sessionUser.getId(), tagId);

        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.tag.delete.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.tag.delete.content", null, locale));

        return "redirect:/notify";
    }

}
