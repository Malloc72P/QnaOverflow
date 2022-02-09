package scra.qnaboard.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.AnswerService;
import scra.qnaboard.dto.answer.AnswerDetailDTO;
import scra.qnaboard.dto.answer.create.CreateAnswerDTO;
import scra.qnaboard.dto.answer.edit.EditAnswerDTO;
import scra.qnaboard.dto.answer.edit.EditAnswerResultDTO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questions/{questionId}/answers")
public class AnswerApiController {

    private final AnswerService answerService;

    @PostMapping
    public String createAnswer(@PathVariable("questionId") long questionId,
                               @RequestBody @Validated CreateAnswerDTO createAnswerDTO,
                               @LoginUser SessionUser sessionUser,
                               Model model) {
        AnswerDetailDTO answer = answerService.createAnswer(sessionUser.getId(), questionId, createAnswerDTO.getContent());

        model.addAttribute("questionId", questionId);
        model.addAttribute("answer", answer);
        return "answer/answer-component";
    }

    @ResponseBody
    @PatchMapping("{answerId}")
    public EditAnswerResultDTO editAnswer(@PathVariable("questionId") long questionId,
                                          @PathVariable("answerId") long answerId,
                                          @RequestBody @Validated EditAnswerDTO editAnswerDTO,
                                          @LoginUser SessionUser sessionUser) {
        return answerService.editAnswer(sessionUser.getId(),
                answerId,
                editAnswerDTO.getContent());
    }

    @ResponseBody
    @DeleteMapping("{answerId}")
    public void deleteAnswer(@PathVariable("questionId") long questionId,
                             @PathVariable("answerId") long answerId,
                             @LoginUser SessionUser sessionUser) {
        answerService.deleteAnswer(sessionUser.getId(), answerId);
    }
}
