package scra.qnaboard.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import scra.qnaboard.dto.notify.NotifyDTO;

@Controller
@RequestMapping("/notify")
public class NotifyController {

    @GetMapping
    public String notify(@ModelAttribute("notify") NotifyDTO notifyDTO) {
        return "commons/notify";
    }
}
