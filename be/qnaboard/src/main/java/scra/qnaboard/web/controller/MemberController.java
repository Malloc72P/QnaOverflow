package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.MemberService;
import scra.qnaboard.web.dto.member.MemberListDTO;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MessageSource message;
    private final HttpSession session;

    @GetMapping
    public String memberList(Model model) {
        MemberListDTO memberListDTO = memberService.findAllMember();
        model.addAttribute("memberListDTO", memberListDTO);

        return "/member/member-list";
    }

    @PostMapping("/sign-out")
    public String signOut(@LoginUser SessionUser sessionUser,
                          RedirectAttributes redirectAttributes,
                          Locale locale) {
        memberService.deleteMember(sessionUser.getId());
        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.members.delete.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.members.delete.content", null, locale));

        session.removeAttribute("user");
        return "redirect:/notify";
    }

    @PostMapping("/log-out")
    public String logout() {
        session.removeAttribute("user");
        return "redirect:/questions";
    }
}
