package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.comment.CommentRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.service.exception.comment.delete.UnauthorizedCommentDeletionException;
import scra.qnaboard.service.exception.comment.edit.CommentEditFailedException;
import scra.qnaboard.service.exception.comment.edit.ForbiddenCommentEditException;
import scra.qnaboard.dto.comment.CommentDTO;
import scra.qnaboard.dto.comment.edit.EditCommentResultDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void 댓글_생성_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String commentContent = "comment-content-1";

        //when
        CommentDTO commentDTO = commentService.createComment(author.getId(), question.getId(), null, commentContent);

        //then
        assertThat(commentDTO.getContent()).isEqualTo(commentContent);
    }

    @Test
    void 댓글_삭제_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String commentContent = "comment-content-1";
        //given
        Comment comment = commentRepository.save(new Comment(author, commentContent, question, null));

        //when
        commentService.deleteComment(author.getId(), comment.getId());

        //then
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(findComment).isNotNull();
        assertThat(findComment.isDeleted()).isTrue();
    }

    @Test
    void 댓글_삭제_실패_테스트_작성자및관리자아님() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String commentContent = "comment-content-1";
        //given
        Comment comment = commentRepository.save(new Comment(author, commentContent, question, null));

        //when & then
        assertThatThrownBy(() -> commentService.deleteComment(anotherAuthor.getId(), comment.getId()))
                .isInstanceOf(UnauthorizedCommentDeletionException.class);
    }

    @Test
    void 댓글_삭제_테스트_관리자는성공해야함() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.ADMIN));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String commentContent = "comment-content-1";
        //given
        Comment comment = commentRepository.save(new Comment(author, commentContent, question, null));

        //when
        commentService.deleteComment(anotherAuthor.getId(), comment.getId());

        //then
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(findComment).isNotNull();
        assertThat(findComment.isDeleted()).isTrue();
    }

    @Test
    void 댓글_수정_테스트() {
        //given
        String commentContent = "comment-content-1";
        String newCommentContent = "new-comment-content-1";
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        Comment comment = commentRepository.save(new Comment(author, commentContent, question, null));

        //when
        EditCommentResultDTO result = commentService.editComment(author.getId(), comment.getId(), newCommentContent);

        //then
        assertThat(result.getContent()).isEqualTo(newCommentContent);
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo(newCommentContent);
    }

    @Test
    void 댓글_수정_실패_테스트_작성자및관리자아님() {
        //given
        String commentContent = "comment-content-1";
        String newCommentContent = "new-comment-content-1";
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        Comment comment = commentRepository.save(new Comment(author, commentContent, question, null));

        //when
        assertThatThrownBy(() -> commentService.editComment(anotherAuthor.getId(), comment.getId(), newCommentContent))
                .isInstanceOf(CommentEditFailedException.class)
                .isInstanceOf(ForbiddenCommentEditException.class);
    }

    @Test
    void 댓글_수정_테스트_관리자는성공해야함() {
        //given
        String commentContent = "comment-content-1";
        String newCommentContent = "new-comment-content-1";
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.ADMIN));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        Comment comment = commentRepository.save(new Comment(author, commentContent, question, null));

        //when
        EditCommentResultDTO result = commentService.editComment(anotherAuthor.getId(), comment.getId(), newCommentContent);

        //then
        assertThat(result.getContent()).isEqualTo(newCommentContent);
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo(newCommentContent);
    }
}
