package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.MemberService;
import scra.qnaboard.web.exception.member.NotLoggedInException;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MessageSource message;
    private final HttpSession session;

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
    public String logout(@LoginUser SessionUser sessionUser) {
        if (sessionUser == null) {
            throw new NotLoggedInException(NotLoggedInException.CAN_NOT_LOGOUT);
        }
        session.removeAttribute("user");
        return "redirect:/questions";
    }
}
