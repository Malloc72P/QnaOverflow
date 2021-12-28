package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import scra.qnaboard.service.QuestionService;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.web.dto.question.list.QuestionListDTO;

/**
 * 질문글에 대한 요청을 처리하는 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 질문글 목록조회 요청을 처리하는 핸들러
     *
     * @return 질문글 목록조회 페이지
     */
    @GetMapping
    public String list(Model model) {
        QuestionListDTO questionListDTO = questionService.questionList();

        model.addAttribute("username", null);
        model.addAttribute("dto", questionListDTO);
        return "/question/question-list";
    }

    @GetMapping("{questionId}")
    public String detail(@PathVariable Long questionId, Model model) {
        QuestionDetailDTO detailDTO = questionService.questionDetail(questionId);
        model.addAttribute(detailDTO);

        return "/question/question-detail";
    }
}
