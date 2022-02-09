package scra.qnaboard.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.dto.comment.CommentDTO;
import scra.qnaboard.dto.comment.create.CreateCommentDTO;
import scra.qnaboard.dto.comment.delete.CommentDeleteResultDTO;
import scra.qnaboard.dto.comment.edit.EditCommentDTO;
import scra.qnaboard.dto.comment.edit.EditCommentResultDTO;
import scra.qnaboard.service.CommentService;

import java.util.Locale;

/**
 * 댓글 API 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentApiController {

    private final CommentService commentService;
    private final MessageSource messageSource;

    /**
     * 댓글 생성 요청을 처리하는 핸들러
     *
     * @param postId      게시글 아이디
     * @param dto         질문글 생성을 위한 파라미터 DTO
     * @param sessionUser 요청을 한 회원정보를 담은 DTO
     * @param model       모델
     * @return 뷰 이름
     */
    @PutMapping
    public String createComment(@PathVariable("postId") long postId,
                                @RequestBody @Validated CreateCommentDTO dto,
                                @LoginUser SessionUser sessionUser,
                                Model model) {
        CommentDTO commentDTO = commentService.createComment(sessionUser.getId(),
                postId,
                dto.getParentCommentId(),
                dto.getContent());
        model.addAttribute("comment", commentDTO);
        return "comment/comment-component";
    }

    /**
     * 댓글 삭제요청을 처리하는 핸들러
     *
     * @param postId      게시글 아이디
     * @param commentId   댓글 아이디
     * @param sessionUser 삭제 요청을 한 회원정보를 담은 DTO
     * @param locale      로케일 정보(AcceptLanguageArgumentResolver를 사용함)
     * @return 댓글 삭제결과(JSON)
     */
    @ResponseBody
    @DeleteMapping("{commentId}")
    public CommentDeleteResultDTO deleteComment(@PathVariable("postId") long postId,
                                                @PathVariable("commentId") long commentId,
                                                @LoginUser SessionUser sessionUser,
                                                Locale locale) {
        commentService.deleteComment(sessionUser.getId(), commentId);
        String content = messageSource.getMessage("ui.comment.delete.content", null, locale);
        String authorName = messageSource.getMessage("ui.comment.delete.author-name", null, locale);
        return new CommentDeleteResultDTO(authorName, content);
    }

    /**
     * 댓글 수정요청을 처리하는 핸들러
     *
     * @param postId      게시글 아이디
     * @param commentId   댓글 아이디
     * @param sessionUser 수정요청을 한 회원정보를 담은 DTO
     * @param dto         댓글수정을 위한 파라미터 DTO
     * @return 댓글 수정결과(JSON)
     */
    @ResponseBody
    @PatchMapping("{commentId}")
    public EditCommentResultDTO patchComment(@PathVariable("postId") long postId,
                                             @PathVariable("commentId") long commentId,
                                             @LoginUser SessionUser sessionUser,
                                             @RequestBody @Validated EditCommentDTO dto) {
        return commentService.editComment(sessionUser.getId(), commentId, dto.getContent());
    }
}
