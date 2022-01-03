package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.service.CommentService;
import scra.qnaboard.web.dto.comment.CommentDTO;
import scra.qnaboard.web.dto.comment.create.CreateCommentDTO;
import scra.qnaboard.web.dto.comment.delete.CommentDeleteResultDTO;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final MessageSource messageSource;

    @PutMapping
    public String createComment(@PathVariable("postId") long postId,
                                @RequestBody @Validated CreateCommentDTO dto,
                                Model model) {
        CommentDTO commentDTO = commentService.createComment(1L, postId, dto.getParentCommentId(), dto.getContent());
        model.addAttribute("comment", commentDTO);
        return "comment/comment-component";
    }

    @ResponseBody
    @DeleteMapping("{commentId}")
    public CommentDeleteResultDTO deleteComment(@PathVariable("postId") long postId,
                                                @PathVariable("commentId") long commentId,
                                                Locale locale) {
        commentService.deleteComment(1L, commentId);
        String content = messageSource.getMessage("ui.comment.delete.content", null, locale);
        String authorName = messageSource.getMessage("ui.comment.delete.author-name", null, locale);
        return new CommentDeleteResultDTO(authorName, content);
    }
}
