package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scra.qnaboard.service.QuestionService;
import scra.qnaboard.web.dto.question.create.CreateQuestionForm;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.web.dto.question.list.QuestionListDTO;

/**
 * 질문글에 대한 요청을 처리하는 컨트롤러
 */
@Slf4j
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

    /**
     * 질문글 상세조회 요청을 처리하는 핸들러
     *
     * @param questionId 대상 질문글의 아이디
     * @param model      뷰에 넘길 모델
     * @return 질문글 상세조회 페이지
     */
    @GetMapping("{questionId}")
    public String detail(@PathVariable Long questionId, Model model) {
        QuestionDetailDTO detailDTO = questionService.questionDetail(questionId);
        model.addAttribute("question", detailDTO);

        return "/question/question-detail";
    }

    @GetMapping("form")
    public String questionForm(@ModelAttribute("questionForm") CreateQuestionForm form) {
        return "/question/question-form";
    }

    @PostMapping
    public String newQuestion(@ModelAttribute("questionForm") @Validated CreateQuestionForm form,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        log.info("form = {}", form);
        log.info("bindingResult = {}", bindingResult);

        if (bindingResult.hasErrors()) {
            return "/question/question-form";
        }

        long newQuestionId = questionService.createQuestion(1L, form.getTitle(), form.getContent());
        redirectAttributes.addAttribute("questionId", newQuestionId);
        return "redirect:/questions/{questionId}";
    }
}
