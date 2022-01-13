package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.AnswerService;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.answer.create.CreateAnswerDTO;
import scra.qnaboard.web.dto.answer.edit.EditAnswerDTO;
import scra.qnaboard.web.dto.answer.edit.EditAnswerResultDTO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;

    @PutMapping
    public String createAnswer(@PathVariable("questionId") long questionId,
                               @RequestBody @Validated CreateAnswerDTO createAnswerDTO,
                               @LoginUser SessionUser sessionUser,
                               Model model) {
        AnswerDetailDTO answer = answerService.createAnswer(sessionUser.getId(), questionId, createAnswerDTO.getContent());

        model.addAttribute("questionId", questionId);
        model.addAttribute("answer", answer);
        return "/answer/answer-component";
    }

    @DeleteMapping("{answerId}")
    @ResponseBody
    public void deleteAnswer(@PathVariable("questionId") long questionId,
                             @PathVariable("answerId") long answerId,
                             @LoginUser SessionUser sessionUser) {
        answerService.deleteAnswer(sessionUser.getId(), answerId);
    }

    @PatchMapping("{answerId}")
    @ResponseBody
    public EditAnswerResultDTO editAnswer(@PathVariable("questionId") long questionId,
                                          @PathVariable("answerId") long answerId,
                                          @RequestBody EditAnswerDTO editAnswerDTO,
                                          @LoginUser SessionUser sessionUser) {
        EditAnswerResultDTO editAnswerResultDTO = answerService.editAnswer(sessionUser.getId(),
                answerId,
                editAnswerDTO.getContent());
        return editAnswerResultDTO;
    }
}
