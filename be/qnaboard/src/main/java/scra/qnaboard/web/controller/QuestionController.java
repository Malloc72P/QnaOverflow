package scra.qnaboard.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 질문글에 대한 요청을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/questions")
public class QuestionController {

    /**
     * 질문글 목록조회 요청을 처리하는 핸들러
     *
     * @return 질문글 목록조회 페이지
     */
    @GetMapping
    public String questionList(Model model) {
        model.addAttribute("username", null);
        return "/question/question-list";
    }
}
