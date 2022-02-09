package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * 홈 컨트롤러
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    @GetMapping
    public String home() {
        return "forward:/questions";
    }
}
