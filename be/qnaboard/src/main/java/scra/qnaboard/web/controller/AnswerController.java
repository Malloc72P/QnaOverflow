package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.service.AnswerService;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.answer.create.CreateAnswerDTO;
import scra.qnaboard.web.dto.answer.edit.EditAnswerDTO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;

    @PutMapping
    public String createAnswer(@PathVariable("questionId") long questionId,
                               @RequestBody @Validated CreateAnswerDTO createAnswerDTO,
                               Model model) {
        AnswerDetailDTO answer = answerService.createAnswer(1L, questionId, createAnswerDTO.getContent());

        model.addAttribute("questionId", questionId);
        model.addAttribute("answer", answer);
        return "/answer/answer-component";
    }

    @DeleteMapping("{answerId}")
    @ResponseBody
    public void deleteAnswer(@PathVariable("questionId") long questionId,
                             @PathVariable("answerId") long answerId) {
        answerService.deleteQuestion(1L, answerId);
    }

    @PatchMapping("{answerId}")
    @ResponseBody
    public void editAnswer(@PathVariable("questionId") long questionId,
                           @PathVariable("answerId") long answerId,
                           @RequestBody EditAnswerDTO editAnswerDTO) {
        answerService.editQuestion(1L, answerId, editAnswerDTO.getContent());
    }
}
