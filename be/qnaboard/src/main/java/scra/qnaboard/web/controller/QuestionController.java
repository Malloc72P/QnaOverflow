package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scra.qnaboard.service.QuestionService;
import scra.qnaboard.service.dto.QuestionOnlyDTO;
import scra.qnaboard.web.dto.question.create.CreateQuestionForm;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.web.dto.question.edit.EditQuestionForm;
import scra.qnaboard.web.dto.question.list.QuestionListDTO;

import java.util.Locale;

/**
 * 질문글에 대한 요청을 처리하는 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final MessageSource message;

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

    /**
     * 질문게시글 입력 폼 요청을 처리함
     *
     * @param form 질문글에 대한 데이터를 담은 DTO(왜 입력에 실패했는지 알려주기 위해서 존재함)
     * @return 질문게시글 입력 폼
     */
    @GetMapping("form")
    public String questionForm(@ModelAttribute("questionForm") CreateQuestionForm form) {
        return "/question/question-form";
    }

    /**
     * 질문 게시글 생성 요청을 처리함
     *
     * @param form               질문게시글의 내용을 담고있는 DTO
     * @param bindingResult      필드에러를 담고 있는 객체
     * @param redirectAttributes 예외처리를 위해 존재함. 이거 덕분에 리다이렉션할때 데이터를 전달할 수 있음
     * @return 실패 - 질문글입력폼 | 성공 - 질문글 상세보기(생성한 질문글)
     */
    @PostMapping
    public String createQuestion(@ModelAttribute("questionForm") @Validated CreateQuestionForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/question/question-form";
        }

        long newQuestionId = questionService.createQuestion(1L, form.getTitle(), form.getContent());
        redirectAttributes.addAttribute("questionId", newQuestionId);
        return "redirect:/questions/{questionId}";
    }

    /**
     * 질문글 삭제요청을 처리함(물리적 삭제 대신 논리적 삭제를 함)
     *
     * @param questionId         삭제할 질문글의 아이디
     * @param redirectAttributes 삭제결과 통보를 위한 객체
     * @param locale             메세지 소스에서 사용할 로케일 정보(Accept 헤더 사용)
     * @return 결과를 통보하는 뷰
     */
    @PostMapping("{questionId}/delete")
    public String delete(@PathVariable long questionId, RedirectAttributes redirectAttributes, Locale locale) {
        questionService.deleteQuestion(1L, questionId);

        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.delete.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.delete.title", null, locale));
        return "redirect:/notify";
    }

    /**
     * 질문게시글 수정 페이지 요청을 처리함
     *
     * @param form       질문글에 대한 데이터를 담은 DTO(왜 입력에 실패했는지 알려주기 위해서 존재함)
     * @param questionId 수정할 질문글 아이디
     * @param model      뷰에 데이터 넘기는 목적의 모델
     * @return 질문게시글 수정 뷰
     */
    @GetMapping("{questionId}/editForm")
    public String questionEditForm(@ModelAttribute("questionEditForm") EditQuestionForm form,
                                   @PathVariable("questionId") long questionId,
                                   Model model) {
        //find a question and put it into a model
        QuestionOnlyDTO questionDTO = questionService.questionOnly(1L, questionId);
        form.update(questionDTO.getTitle(), questionDTO.getContent());

        model.addAttribute("questionId", questionId);
        model.addAttribute("questionEditForm", form);
        return "/question/question-edit-form";
    }

    /**
     * 질문게시글 수정요청 처리
     *
     * @param form               질문게시글 수정 정보
     * @param questionId         수정할 질문글의 아이디
     * @param bindingResult      필드 에러를 담고 있는 객체
     * @param redirectAttributes 예외처리를 위해 존재함. 이거 덕분에 리다이렉션할때 데이터를 전달할 수 있음
     * @return 질문 상세보기 페이지
     */
    @PostMapping("edit/{questionId}")
    public String editQuestion(@ModelAttribute("questionEditForm") @Validated EditQuestionForm form,
                               @PathVariable("questionId") long questionId,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/question/question-edit-form";
        }
        //edit question
        questionService.editQuestion(1L, questionId, form.getTitle(), form.getContent());
        redirectAttributes.addAttribute("questionId", questionId);
        return "redirect:/questions/{questionId}";
    }

}
