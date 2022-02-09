package scra.qnaboard.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.dto.answer.AnswerDetailDTO;
import scra.qnaboard.dto.answer.create.CreateAnswerDTO;
import scra.qnaboard.dto.answer.edit.EditAnswerDTO;
import scra.qnaboard.dto.answer.edit.EditAnswerResultDTO;
import scra.qnaboard.service.AnswerService;

/**
 * 답변글 API 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/questions/{questionId}/answers")
public class AnswerApiController {

    private final AnswerService answerService;

    /**
     * 답변글 생성 요청을 처리하는 핸들러
     *
     * @param questionId      질문글 아이디
     * @param createAnswerDTO 답변글 생성을 위한 파라미터 DTO
     * @param sessionUser     생성요청을 한 회원정보를 담은 DTO
     * @param model           모델
     * @return 뷰 이름
     */
    @PostMapping
    public String createAnswer(@PathVariable("questionId") long questionId,
                               @RequestBody @Validated CreateAnswerDTO createAnswerDTO,
                               @LoginUser SessionUser sessionUser,
                               Model model) {
        //답변글 생성 로직 수행
        AnswerDetailDTO answer = answerService.createAnswer(sessionUser.getId(), questionId, createAnswerDTO.getContent());
        //생성결과를 모델에 담음
        model.addAttribute("questionId", questionId);
        model.addAttribute("answer", answer);
        return "answer/answer-component";
    }

    /**
     * 답변글 수정요청을 처리하는 핸들러
     *
     * @param questionId    질문글 아이디
     * @param answerId      답변글 아이디
     * @param editAnswerDTO 답변글 수정 파라미터 DTO
     * @param sessionUser   수정요청을 한 회원정보를 담은 DTO
     * @return 답변글 수정결과를 담은 JSON
     */
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

    /**
     * 답변글 삭제 요청을 처리하는 핸들러
     *
     * @param questionId  질문글 아이디
     * @param answerId    답변글 아이디
     * @param sessionUser 삭제요청을 한 회원정보를 담은 DTO
     */
    @ResponseBody
    @DeleteMapping("{answerId}")
    public void deleteAnswer(@PathVariable("questionId") long questionId,
                             @PathVariable("answerId") long answerId,
                             @LoginUser SessionUser sessionUser) {
        answerService.deleteAnswer(sessionUser.getId(), answerId);
    }
}
