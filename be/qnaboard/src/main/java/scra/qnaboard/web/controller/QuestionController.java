package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.QuestionService;
import scra.qnaboard.service.SearchInputParserService;
import scra.qnaboard.service.dto.QuestionWithTagDTO;
import scra.qnaboard.web.dto.page.Paging;
import scra.qnaboard.web.dto.question.create.CreateQuestionForm;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.web.dto.question.edit.EditQuestionForm;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;
import scra.qnaboard.web.dto.question.search.ParsedSearchQuestionDTO;
import scra.qnaboard.web.dto.question.search.SearchQuestionDTO;

import java.util.Locale;

/**
 * 질문글에 대한 요청을 처리하는 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private static final String defaultPageNumber = "0";
    private static final String defaultPageSize = "20";

    private final MessageSource message;
    private final QuestionService questionService;
    private final SearchInputParserService searchInputService;

    /**
     * 질문글 목록조회 요청을 처리하는 핸들러
     *
     * @return 질문글 목록조회 페이지
     */
    @GetMapping
    public String search(@RequestParam(defaultValue = defaultPageNumber) int pageNumber,
                         @RequestParam(defaultValue = defaultPageSize) int pageSize,
                         SearchQuestionDTO searchDTO,
                         Model model) {
        ParsedSearchQuestionDTO parsedInput = searchInputService.parse(searchDTO);
        Page<QuestionSummaryDTO> questionPage = questionService.searchQuestions(parsedInput, pageNumber, pageSize);

        Paging<QuestionSummaryDTO> paging = Paging.buildPaging(questionPage, parsedInput.searchInput());

        model.addAttribute("paging", paging);

        return "question/question-list";
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

        return "question/question-detail";
    }

    /**
     * 질문게시글 입력 폼 요청을 처리함
     *
     * @param form 질문글에 대한 데이터를 담은 DTO(왜 입력에 실패했는지 알려주기 위해서 존재함)
     * @return 질문게시글 입력 폼
     */
    @GetMapping("form")
    public String questionForm(@ModelAttribute("questionForm") CreateQuestionForm form) {
        return "question/question-form";
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
                                 @LoginUser SessionUser sessionUser,
                                 RedirectAttributes redirectAttributes) {
        //생성 폼에 문제가 있는지 확인함. 문제가 있다면 생성 페이지로 돌려보냄
        if (bindingResult.hasErrors()) {
            return "question/question-form";
        }

        //질문글을 생성하고, 생성된 질문글의 아이디에 대한 상세 페이지 요청을 하도록 리다이렉션시킴
        long newQuestionId = questionService.createQuestion(sessionUser.getId(),
                form.getTitle(),
                form.getContent(),
                form.extractTagIds());

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
    public String delete(
            @PathVariable long questionId,
            RedirectAttributes redirectAttributes,
            @LoginUser SessionUser sessionUser,
            Locale locale) {
        //질문글 삭제
        questionService.deleteQuestion(sessionUser.getId(), questionId);

        //삭제완료를 알리는 페이지로 리다이렉션 시킴. 필요한 정보는 요청 파라미터에 넣어줌
        redirectAttributes.addAttribute("title", message.getMessage("ui.notify.question.delete.title", null, locale));
        redirectAttributes.addAttribute("content", message.getMessage("ui.notify.question.delete.content", null, locale));
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
        //질문글을 조회해서 폼의 내용을 업데이트함(이 내용이 질문글 수정페이지로 전달됨)
        QuestionWithTagDTO questionDTO = questionService.questionWithTag(questionId);
        form.update(questionDTO.getTitle(), questionDTO.getContent());

        //질문글의 아이디까지 모델에 담아서 넘겨줌
        model.addAttribute("questionId", questionId);
        model.addAttribute("tags", questionDTO.getTags());
        return "question/question-edit-form";
    }

    /**
     * 질문게시글 수정요청 처리 <br>
     * 수정 폼에 잘못 입력해서 validation에 실패했다면, bindingResult.hasError로 검사해서 실패 여부를 알아낸다 <br>
     * 실패했다면 즉시 수정 폼 뷰를 반환한다. ModelAttribute를 사용하므로, 사용자가 이전에 입력했던 내용은 모델에 이미 담겨있는 상태이다. <br>
     * 덕분에 사용자에게 왜 입력이 실패했는지를 알려줄 수 있다.
     *
     * @param form               질문게시글 수정 정보
     * @param questionId         수정할 질문글의 아이디
     * @param bindingResult      필드 에러를 담고 있는 객체
     * @param redirectAttributes 예외처리를 위해 존재함. 이거 덕분에 리다이렉션할때 데이터를 전달할 수 있음
     * @return 질문 상세보기 페이지
     */
    @PostMapping("{questionId}/edit")
    public String editQuestion(
            @ModelAttribute("questionEditForm") @Validated EditQuestionForm form,
            BindingResult bindingResult,
            @PathVariable("questionId") long questionId,
            @LoginUser SessionUser sessionUser,
            RedirectAttributes redirectAttributes) {

        //입력 폼에 문제가 있는지 확인
        if (bindingResult.hasErrors()) {
            return "question/question-edit-form";
        }

        //질문글 수정 후 상세 페이지로 리다이렉션
        questionService.editQuestion(
                sessionUser.getId(),
                questionId,
                form.getTitle(),
                form.getContent(),
                form.extractTagIds());

        redirectAttributes.addAttribute("questionId", questionId);
        return "redirect:/questions/{questionId}";
    }

}
