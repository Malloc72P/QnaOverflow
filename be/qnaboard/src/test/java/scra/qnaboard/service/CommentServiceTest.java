package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.comment.CommentRepository;
import scra.qnaboard.domain.repository.comment.CommentSimpleQueryRepository;
import scra.qnaboard.service.exception.comment.delete.CommentDeleteFailedException;
import scra.qnaboard.service.exception.comment.edit.CommentEditFailedException;
import scra.qnaboard.service.exception.comment.edit.UnauthorizedCommentEditException;
import scra.qnaboard.web.dto.comment.CommentDTO;
import scra.qnaboard.web.dto.comment.edit.EditCommentResultDTO;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private PostService postService;
    @Mock
    private MemberService memberService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentSimpleQueryRepository commentSimpleQueryRepository;

    @Test
    void 댓글_생성_테스트() throws Exception {
        //given
        long memberId = 1L;
        long questionId = 2L;
        long commentId = 3L;
        Long parentCommentId = null;
        String title = "question-title";
        String content = "question-content";
        String commentContent = "comment-content";
        //given
        Member member = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", memberId);
        //given
        Question question = new Question(member, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Comment commentNoId = new Comment(member, commentContent, question, null);
        Comment comment = new Comment(member, commentContent, question, null);
        ReflectionTestUtils.setField(comment, "id", commentId);
        //given
        CommentDTO commentDTO = CommentDTO.builder()
                .content(commentContent)
                .authorName(member.getNickname())
                .authorId(memberId)
                .parentCommentId(parentCommentId)
                .parentPostId(questionId)
                .commentId(commentId)
                .build();
        //given
        given(memberService.findMember(memberId)).willReturn(member);
        given(postService.findPostById(questionId)).willReturn(question);
        given(commentRepository.save(commentNoId)).willReturn(comment);

        //when
        CommentDTO result = commentService.createComment(memberId, questionId, parentCommentId, commentContent);

        //then
        assertThat(result).extracting(
                CommentDTO::getAuthorName,
                CommentDTO::getAuthorId,
                CommentDTO::getParentCommentId,
                CommentDTO::getParentPostId,
                CommentDTO::getCommentId,
                CommentDTO::getContent
        ).containsExactly(
                commentDTO.getAuthorName(),
                commentDTO.getAuthorId(),
                commentDTO.getParentCommentId(),
                commentDTO.getParentPostId(),
                commentDTO.getCommentId(),
                commentDTO.getContent()
        );
    }

    @Test
    void 댓글_수정_테스트() throws Exception {
        //given
        long memberId = 1L;
        long questionId = 2L;
        long commentId = 3L;
        String title = "question-title";
        String content = "question-content";
        String commentContent = "comment-content";
        String newCommentContent = "new-comment-content";
        //given
        Member member = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", memberId);
        //given
        Question question = new Question(member, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Comment comment = new Comment(member, commentContent, question, null);
        ReflectionTestUtils.setField(comment, "id", commentId);
        //given
        EditCommentResultDTO editCommentResultDTO = new EditCommentResultDTO(newCommentContent);
        //given
        given(memberService.findMember(memberId)).willReturn(member);
        given(commentSimpleQueryRepository.commentWithAuthor(commentId)).willReturn(Optional.of(comment));

        //when
        EditCommentResultDTO result = commentService.editComment(memberId, commentId, newCommentContent);

        //then
        assertThat(result.getContent()).isEqualTo(editCommentResultDTO.getContent());

    }

    @Test
    void 댓글_수정_실패_테스트_작성자및관리자아님() throws Exception {
        //given
        long memberId = 1L;
        long questionId = 2L;
        long commentId = 3L;
        long anotherMemberId = 4L;
        String title = "question-title";
        String content = "question-content";
        String commentContent = "comment-content";
        String newCommentContent = "new-comment-content";
        //given
        Member member = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", memberId);
        Member anotherMember = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", anotherMemberId);
        //given
        Question question = new Question(member, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Comment comment = new Comment(member, commentContent, question, null);
        ReflectionTestUtils.setField(comment, "id", commentId);
        //given
        given(memberService.findMember(anotherMemberId)).willReturn(anotherMember);
        given(commentSimpleQueryRepository.commentWithAuthor(commentId)).willReturn(Optional.of(comment));

        //when & then
        assertThatThrownBy(() -> commentService.editComment(anotherMemberId, commentId, newCommentContent))
                .isInstanceOf(CommentEditFailedException.class)
                .isInstanceOf(UnauthorizedCommentEditException.class);
    }

    @Test
    void 댓글_수정_테스트_관리자는성공해야함() throws Exception {
        //given
        long memberId = 1L;
        long questionId = 2L;
        long commentId = 3L;
        long anotherMemberId = 4L;
        String title = "question-title";
        String content = "question-content";
        String commentContent = "comment-content";
        String newCommentContent = "new-comment-content";
        //given
        Member member = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", memberId);
        Member anotherMember = new Member("member", MemberRole.ADMIN);
        ReflectionTestUtils.setField(member, "id", anotherMemberId);
        //given
        Question question = new Question(member, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Comment comment = new Comment(member, commentContent, question, null);
        ReflectionTestUtils.setField(comment, "id", commentId);
        //given
        EditCommentResultDTO editCommentResultDTO = new EditCommentResultDTO(newCommentContent);
        //given
        given(memberService.findMember(anotherMemberId)).willReturn(anotherMember);
        given(commentSimpleQueryRepository.commentWithAuthor(commentId)).willReturn(Optional.of(comment));

        //when
        EditCommentResultDTO result = commentService.editComment(anotherMemberId, commentId, newCommentContent);
        //then
        assertThat(result.getContent()).isEqualTo(editCommentResultDTO.getContent());
    }

    @Test
    void 댓글_삭제_테스트() throws Exception {
        //given
        long memberId = 1L;
        long questionId = 2L;
        long commentId = 3L;
        String title = "question-title";
        String content = "question-content";
        String commentContent = "comment-content";
        //given
        Member member = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", memberId);
        //given
        Question question = new Question(member, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Comment comment = new Comment(member, commentContent, question, null);
        ReflectionTestUtils.setField(comment, "id", commentId);

        //given
        given(memberService.findMember(memberId)).willReturn(member);
        given(commentSimpleQueryRepository.commentWithAuthor(commentId)).willReturn(Optional.of(comment));

        //when & then
        commentService.deleteComment(memberId, commentId);
    }

    @Test
    void 댓글_삭제_실패_테스트_작성자및관리자아님() throws Exception {
        //given
        long memberId = 1L;
        long anotherMemberId = 1L;
        long questionId = 2L;
        long commentId = 3L;
        String title = "question-title";
        String content = "question-content";
        String commentContent = "comment-content";
        //given
        Member member = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", memberId);
        Member anotherMember = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", anotherMemberId);
        //given
        Question question = new Question(member, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Comment comment = new Comment(member, commentContent, question, null);
        ReflectionTestUtils.setField(comment, "id", commentId);

        //given
        given(memberService.findMember(anotherMemberId)).willReturn(anotherMember);
        given(commentSimpleQueryRepository.commentWithAuthor(commentId)).willReturn(Optional.of(comment));

        //when & then
        assertThatThrownBy(() -> commentService.deleteComment(memberId, commentId))
                .isInstanceOf(CommentDeleteFailedException.class);

    }

    @Test
    void 댓글_삭제_테스트_관리자는성공해야함() throws Exception {
        //given
        long memberId = 1L;
        long anotherMemberId = 1L;
        long questionId = 2L;
        long commentId = 3L;
        String title = "question-title";
        String content = "question-content";
        String commentContent = "comment-content";
        //given
        Member member = new Member("member", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", memberId);
        Member anotherMember = new Member("member", MemberRole.ADMIN);
        ReflectionTestUtils.setField(member, "id", anotherMemberId);
        //given
        Question question = new Question(member, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Comment comment = new Comment(member, commentContent, question, null);
        ReflectionTestUtils.setField(comment, "id", commentId);

        //given
        given(memberService.findMember(anotherMemberId)).willReturn(anotherMember);
        given(commentSimpleQueryRepository.commentWithAuthor(commentId)).willReturn(Optional.of(comment));

        //when & then
        commentService.deleteComment(memberId, commentId);
    }
}
