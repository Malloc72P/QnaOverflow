package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
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
import scra.qnaboard.dto.page.Paging;
import scra.qnaboard.dto.tag.create.CreateTagForm;
import scra.qnaboard.dto.tag.edit.EditTagForm;
import scra.qnaboard.dto.tag.list.TagDTO;
import scra.qnaboard.service.TagService;

import java.util.Locale;

/**
 * 태그 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private static final String defaultPageNumber = "0";
    private static final String defaultPageSize = "12";

    private final TagService tagService;
    private final MessageSource message;

    /**
     * 태그 목록조회 요청을 처리하는 핸들러
     *
     * @param pageNumber 페이지 번호
     * @param pageSize   페이지 크기
     * @param model      모델
     * @return 뷰 이름
     */
    @GetMapping
    public String list(@RequestParam(defaultValue = defaultPageNumber) int pageNumber,
                       @RequestParam(defaultValue = defaultPageSize) int pageSize,
                       Model model) {
        //태그 목록을 조회함
        Page<TagDTO> tagDTOPage = tagService.tagList(pageNumber, pageSize);
        //페이지네이션을 위한 DTO로 변환해서 모델이 등록함
        Paging<TagDTO> tagDTOPaging = Paging.buildPaging(tagDTOPage);
        model.addAttribute("paging", tagDTOPaging);
        return "tag/tag-list";
    }

    /**
     * 태그 생성 폼 요청을 처리하는 핸들러
     * 사용자의 실수로 생성에 실패한 경우, CreateTagForm타입의 DTO를 사용해서 왜 실패했는지를 알려준다
     *
     * @param form 태그 생성 폼의 정보를 담은 DTO
     * @return 뷰 이름
     */
    @GetMapping("form")
    public String tagForm(@ModelAttribute("tagForm") CreateTagForm form) {
        return "tag/tag-form";
    }

    /**
     * 태그 생성 요청을 처리하는 핸들러
     * 생성요청 폼에 문제가 있어서 태그 생성에 실패하는 경우, 생성 폼 요청으로 리다이렉션시켜서 왜 실패했는지 알려준다.
     *
     * @param form               태그생성 DTO
     * @param bindingResult      DTO바인딩 오류
     * @param sessionUser        생성요청을 한 회원정보를 담은 DTO
     * @param redirectAttributes 리다이렉션 파라미터
     * @param locale             로케일
     * @return 뷰의 이름
     */
    @PostMapping
    public String create(@ModelAttribute("tagForm") @Validated CreateTagForm form,
                         BindingResult bindingResult,
                         @LoginUser SessionUser sessionUser,
                         RedirectAttributes redirectAttributes,
                         Locale locale) {
        //바인딩에 실패한 경우, 태그생성폼을 반환함
        if (bindingResult.hasErrors()) {
            return "tag/tag-form";
        }
        //태그 생성
        tagService.createTag(sessionUser.getId(), form.getName(), form.getDescription());
        //생성결과를 redirectAttributes에 담고 리다이렉션한다
        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.tag.create.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.tag.create.content", null, locale));
        return "redirect:/notify";
    }

    /**
     * 태그 수정 폼 요청을 처리하는 핸들러
     * 생성폼 핸들러와 마찬가지로, 사용자 입력실수를 알려주기 위해 EditTagForm을 사용한다
     *
     * @param tagId 수정할 태그의 아이디
     * @param form  태그 수정 폼 파라미터 DTO
     * @param model 모델
     * @return 뷰의 이름
     */
    @GetMapping("{tagId}/edit-form")
    public String tagEditForm(@PathVariable("tagId") long tagId,
                              @ModelAttribute("editTagForm") EditTagForm form,
                              Model model) {
        TagDTO tagDTO = tagService.tagById(tagId);
        form.update(tagDTO.getTagName(), tagDTO.getTagDescription());
        model.addAttribute("tagId", tagId);
        return "tag/tag-edit-form";
    }

    /**
     * 태그 수정요청을 처리하는 핸들러
     *
     * @param tagId              수정대상 태그의 아이디
     * @param form               태그 수정 폼 파라미터 DTO
     * @param bindingResult      DTO바인딩 오류
     * @param redirectAttributes 리다이렉션 파라미터
     * @param sessionUser        수정 요청을 한 회원정보를 담은 DTO
     * @param locale             로케일
     * @return 뷰의 이름
     */
    @PostMapping("{tagId}/edit")
    public String edit(@PathVariable("tagId") long tagId,
                       @ModelAttribute("editTagForm") @Validated EditTagForm form,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       @LoginUser SessionUser sessionUser,
                       Locale locale) {
        //바인딩에 문제가 있으면 수정폼을 반환함
        if (bindingResult.hasErrors()) {
            return "tag/tag-edit-form";
        }
        //태그 수정로직을 수행하고, 그 결과를 redirectAttributes에 담은 후 알림페이지로 리다이렉션한다
        tagService.editTag(sessionUser.getId(), tagId, form.getName(), form.getDescription());
        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.tag.edit.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.tag.edit.content", null, locale));
        return "redirect:/notify";
    }

    /**
     * 태그 삭제요청을 처리하는 핸들러
     *
     * @param tagId              삭제할 태그의 아이디
     * @param redirectAttributes 리다이렉션 파라미터
     * @param sessionUser        삭제요청을 한 회원정보를 담은 DTO
     * @param locale             로케일
     * @return 뷰의 이름
     */
    @PostMapping("{tagId}/delete")
    public String delete(@PathVariable("tagId") long tagId,
                         RedirectAttributes redirectAttributes,
                         @LoginUser SessionUser sessionUser,
                         Locale locale) {
        //태그 삭제후, 결과를 redirectAttributes에 담아서 알림페이지로 리다이렉션
        tagService.deleteTag(sessionUser.getId(), tagId);
        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.tag.delete.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.tag.delete.content", null, locale));
        return "redirect:/notify";
    }

}
