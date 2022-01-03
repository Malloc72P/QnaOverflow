package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.repository.CommentRepository;
import scra.qnaboard.service.exception.comment.CommentNotFoundException;
import scra.qnaboard.service.exception.post.PostNotFoundException;
import scra.qnaboard.web.dto.comment.CommentDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentDTO createComment(long requesterId, long postId, Long parentCommentId, String content) {
        Member member = memberService.findMember(requesterId);
        Post parentPost = postService.findPostById(postId);
        Comment parentComment = findCommentParentById(parentCommentId);

        Comment comment = new Comment(member, content, parentPost, parentComment);
        commentRepository.save(comment);

        return CommentDTO.from(comment);
    }

    private Comment findCommentParentById(Long commentId) {
        return commentId == null ? null : findComment(commentId);
    }

    private Comment findComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }
}
