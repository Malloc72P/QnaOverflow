package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import scra.qnaboard.service.AnswerService;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.answer.create.CreateAnswerDTO;

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

        model.addAttribute("answer", answer);
        return "/answer/answer-component";
    }
}
