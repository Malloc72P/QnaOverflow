package scra.qnaboard.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.CommentService;
import scra.qnaboard.dto.comment.CommentDTO;
import scra.qnaboard.dto.comment.create.CreateCommentDTO;
import scra.qnaboard.dto.comment.delete.CommentDeleteResultDTO;
import scra.qnaboard.dto.comment.edit.EditCommentDTO;
import scra.qnaboard.dto.comment.edit.EditCommentResultDTO;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentApiController {

    private final CommentService commentService;
    private final MessageSource messageSource;

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

    @ResponseBody
    @PatchMapping("{commentId}")
    public EditCommentResultDTO patchComment(@PathVariable("postId") long postId,
                                             @PathVariable("commentId") long commentId,
                                             @LoginUser SessionUser sessionUser,
                                             @RequestBody @Validated EditCommentDTO dto) {
        return commentService.editComment(sessionUser.getId(), commentId, dto.getContent());
    }
}
