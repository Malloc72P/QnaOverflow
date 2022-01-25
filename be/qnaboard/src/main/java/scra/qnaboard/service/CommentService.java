package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.repository.comment.CommentRepository;
import scra.qnaboard.domain.repository.comment.CommentSimpleQueryRepository;
import scra.qnaboard.service.exception.comment.AlreadyDeletedCommentException;
import scra.qnaboard.service.exception.comment.CommentNotFoundException;
import scra.qnaboard.service.exception.comment.delete.UnauthorizedCommentDeletionException;
import scra.qnaboard.service.exception.comment.edit.UnauthorizedCommentEditException;
import scra.qnaboard.web.dto.comment.CommentDTO;
import scra.qnaboard.web.dto.comment.edit.EditCommentResultDTO;

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
        comment = commentRepository.save(comment);

        return CommentDTO.from(comment);
    }

    /**
     * 댓글을 수정하는 메서드
     *
     * @param requesterId 요청한 유저의 아이디
     * @param commentId   수정할 댓글의 아이디
     * @param content     댓글의 새로운 내용
     */
    @Transactional
    public EditCommentResultDTO editComment(long requesterId, long commentId, String content) {
        Comment comment = commentWithAuthor(commentId);
        Member requester = memberService.findMember(requesterId);

        //이미 삭제된 댓글인 경우 예외를 발생시켜야 함
        if (comment.isDeleted()) {
            throw new AlreadyDeletedCommentException(commentId);
        }

        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && comment.isNotOwner(requester)) {
            throw new UnauthorizedCommentEditException(commentId, requesterId);
        }

        comment.update(content);

        return new EditCommentResultDTO(content);
    }

    @Transactional
    public void deleteComment(long requesterId, long commentId) {
        Comment comment = commentWithAuthor(commentId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && comment.isNotOwner(requester)) {
            throw new UnauthorizedCommentDeletionException(commentId, requesterId);
        }

        //관리자이거나 질문게시글의 소유자면 질문게시글 삭제함
        //관리자는 다른 관리자의 게시글을 지울 수 있음
        comment.delete();
    }

    public Comment commentWithAuthor(long commentId) {
        return commentSimpleQueryRepository.commentWithAuthor(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    private Comment findCommentParentById(Long commentId) {
        return commentId == null ? null : findComment(commentId);
    }

    private Comment findComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }
}
