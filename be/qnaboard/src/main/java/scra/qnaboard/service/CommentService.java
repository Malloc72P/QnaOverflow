package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.repository.comment.CommentRepository;
import scra.qnaboard.domain.repository.comment.CommentSimpleQueryRepository;
import scra.qnaboard.dto.comment.CommentDTO;
import scra.qnaboard.dto.comment.edit.EditCommentResultDTO;
import scra.qnaboard.service.exception.comment.AlreadyDeletedCommentException;
import scra.qnaboard.service.exception.comment.AlreadyDeletedParentCommentException;
import scra.qnaboard.service.exception.comment.CommentNotFoundException;
import scra.qnaboard.service.exception.comment.delete.UnauthorizedCommentDeletionException;
import scra.qnaboard.service.exception.comment.edit.UnauthorizedCommentEditException;

/**
 * 댓글 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final CommentSimpleQueryRepository commentSimpleQueryRepository;

    /**
     * 댓글 생성 메서드
     *
     * @param requesterId     요청자의 아이디
     * @param postId          댓글의 부모 게시글
     * @param parentCommentId 댓글의 부모 댓글
     * @param content         댓글의 내용
     * @return 새로 생성된 댓글의 정보를 담은 DTO
     */
    @Transactional
    public CommentDTO createComment(long requesterId, long postId, Long parentCommentId, String content) {
        //요청자, 부모게시글, 부모댓글 엔티티를 조회함
        Member member = memberService.findMember(requesterId);
        Post parentPost = postService.findPostById(postId);
        Comment parentComment = findCommentParentById(parentCommentId);

        //부모댓글이 존재하긴 하는데 지워졌다면 예외가 발생해야함
        if (parentComment != null && parentComment.isDeleted()) {
            throw new AlreadyDeletedParentCommentException(parentCommentId);
        }

        //댓글 엔티티를 생성하고 저장
        Comment comment = new Comment(member, content, parentPost, parentComment);
        comment = commentRepository.save(comment);

        //DTO를 만들어서 반환
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

        //이미 삭제된 댓글인 경우 수정할 수 없음
        if (comment.isDeleted()) {
            throw new AlreadyDeletedCommentException(commentId);
        }

        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && comment.isNotOwner(requester)) {
            throw new UnauthorizedCommentEditException(commentId, requesterId);
        }

        //댓글 수정
        comment.update(content);

        //업데이트된 댓글의 정보를 담은 DTO 반환
        return new EditCommentResultDTO(content);
    }

    /**
     * 댓글 삭제.
     * 관리자는 자신이 댓글의 작성자가 아니어도 댓글을 지울 수 있음
     */
    @Transactional
    public void deleteComment(long requesterId, long commentId) {
        //댓글과 요청자 엔티티 조회
        Comment comment = commentWithAuthor(commentId);
        Member requester = memberService.findMember(requesterId);
        //이미 삭제된 경우 또 삭제할 수 없음
        if (comment.isDeleted()) {
            throw new AlreadyDeletedCommentException(commentId);
        }
        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && comment.isNotOwner(requester)) {
            throw new UnauthorizedCommentDeletionException(commentId, requesterId);
        }
        //관리자이거나 질문게시글의 소유자면 질문게시글 삭제함
        //관리자는 다른 관리자의 게시글을 지울 수 있음
        comment.delete();
    }

    /**
     * 댓글과 작성자 엔티티를 함께 조회함
     */
    private Comment commentWithAuthor(long commentId) {
        return commentSimpleQueryRepository.commentWithAuthor(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    /**
     * 부모 댓글 엔티티를 찾아서 반환함
     * 부모 댓글의 아이디가 null이면 null을 반환함
     */
    private Comment findCommentParentById(Long commentId) {
        return commentId == null ? null : findComment(commentId);
    }

    /**
     * 댓글 엔티티 조회 메서드
     */
    private Comment findComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }
}
