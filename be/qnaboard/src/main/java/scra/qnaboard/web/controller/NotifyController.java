package scra.qnaboard.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import scra.qnaboard.dto.notify.NotifyDTO;

/**
 * 알림페이지 컨트롤러
 */
@Controller
@RequestMapping("/notify")
public class NotifyController {

    /**
     * 알림페이지 요청을 처리하는 핸들러.
     * 대부분의 경우 리다이렉션에 의해 해당 핸들러가 실행된다
     * (ex: 질문글 삭제요청 성공 후, 삭제가 완료되었다고 사용자에게 알려주기 위해 해당 핸들러로 리다이렉션함)
     * @param notifyDTO 알림페이지의 화면구현에 필요한 정보를 담은 DTO.
     * @return 뷰 이름
     */
    @GetMapping
    public String notify(@ModelAttribute("notify") NotifyDTO notifyDTO) {
        return "commons/notify";
    }
}
