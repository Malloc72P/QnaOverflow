package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import scra.qnaboard.service.CommentService;
import scra.qnaboard.web.dto.comment.CommentDTO;
import scra.qnaboard.web.dto.comment.create.CreateCommentDTO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PutMapping
    public String createComment(@PathVariable("postId") long postId,
                                @RequestBody @Validated CreateCommentDTO dto,
                                Model model) {
        CommentDTO commentDTO = commentService.createComment(1L, postId, dto.getParentCommentId(), dto.getContent());
        model.addAttribute("comment", commentDTO);
        return "comment/comment-component";
    }
}
