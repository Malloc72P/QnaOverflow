package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.repository.comment.CommentRepository;
import scra.qnaboard.domain.repository.comment.CommentSimpleQueryRepository;
import scra.qnaboard.service.exception.comment.CommentNotFoundException;
import scra.qnaboard.service.exception.comment.delete.CommentDeleteFailedException;
import scra.qnaboard.web.dto.comment.CommentDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final CommentSimpleQueryRepository commentSimpleQueryRepository;

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

    @Transactional
    public void deleteComment(long requesterId, long commentId) {
        Comment comment = commentWithAuthor(commentId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && comment.isNotOwner(requester)) {
            throw new CommentDeleteFailedException(CommentDeleteFailedException.UNAUTHORIZED, commentId, requesterId);
        }

        //관리자이거나 질문게시글의 소유자면 질문게시글 삭제함
        //관리자는 다른 관리자의 게시글을 지울 수 있음
        comment.delete();
    }

    public Comment commentWithAuthor(long commentId) {
        return commentSimpleQueryRepository.commentWithAuthor(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }
}
