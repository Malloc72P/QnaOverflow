package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.dto.member.MemberDTO;
import scra.qnaboard.dto.page.Paging;
import scra.qnaboard.service.MemberService;

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * 회원 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private static final String defaultPageNumber = "0";
    private static final String defaultPageSize = "30";

    private final MemberService memberService;
    private final MessageSource message;
    private final HttpSession session;

    /**
     * 회원 목록조회를 위한 컨트롤러
     *
     * @param pageNumber 페이지 번호
     * @param pageSize   페이지 크기
     * @param model      모델
     * @return 뷰 이름
     */
    @GetMapping
    public String memberList(@RequestParam(defaultValue = defaultPageNumber) int pageNumber,
                             @RequestParam(defaultValue = defaultPageSize) int pageSize,
                             Model model) {
        //회원 목록조회를 함
        Page<MemberDTO> memberDTOPage = memberService.members(pageNumber, pageSize);
        //조회결과를 가지고 페이지네이션을 위한 DTO 객체를 생성하고 모델에 담는다
        Paging<MemberDTO> paging = Paging.buildPaging(memberDTOPage);
        model.addAttribute("paging", paging);
        return "member/member-list";
    }

    /**
     * 회원 탈퇴 요청을 처리하는 핸들러.
     * 회원 탈퇴를 하면 회원 목록조회에서 더 이상 조회되지 않는다.
     * 만약 같은 소셜계정으로 로그인하게 되면, 해당 계정은 다시 활성화되고, 회원 목록조회에 다시 노출된다.
     *
     * @param sessionUser        탈퇴 요청을 한 회원정보를 담은 DTO
     * @param redirectAttributes 리다이렉트 파라미터
     * @param locale             로케일
     * @return 리다이렉트될 경로
     */
    @PostMapping("/sign-out")
    public String signOut(@LoginUser SessionUser sessionUser,
                          RedirectAttributes redirectAttributes,
                          Locale locale) {
        //회원을 비활성화하고, 그 결과를 redirectAttributes에 담는다
        memberService.deleteMember(sessionUser.getId());
        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.members.delete.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.members.delete.content", null, locale));
        //세션에서 회원정보를 제거한다
        session.removeAttribute("user");
        return "redirect:/notify";
    }

    /**
     * 로그아웃 요청을 처리하는 핸들러
     *
     * @return 리다이렉트 경로
     */
    @PostMapping("/log-out")
    public String logout() {
        session.removeAttribute("user");
        return "redirect:/questions";
    }
}
