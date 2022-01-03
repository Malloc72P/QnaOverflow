package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.comment.CommentRepository;
import scra.qnaboard.service.exception.comment.CommentNotFoundException;
import scra.qnaboard.service.exception.comment.delete.CommentDeleteFailedException;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;
import scra.qnaboard.web.dto.comment.CommentDTO;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("코멘트 서비스로 댓글을 생성할 수 있어야 합니다")
    void testCreateComment() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Member member = dataDTO.noneAdminMember();

        dataDTO.questionStream().forEach(question -> {
            String testContent = "test-comment";
            CommentDTO commentDTO = commentService.createComment(member.getId(), question.getId(), null, testContent);

            Comment comment = em.createQuery("select c from Comment  c where c.id = :id", Comment.class)
                    .setParameter("id", commentDTO.getCommentId())
                    .getSingleResult();

            assertThat(commentDTO).extracting(
                    CommentDTO::getCommentId,
                    CommentDTO::getContent,
                    CommentDTO::getCreatedDate,
                    CommentDTO::getAuthorId,
                    CommentDTO::getAuthorName,
                    CommentDTO::getParentCommentId
            ).containsExactly(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedDate(),
                    comment.getAuthor().getId(),
                    comment.getAuthor().getNickname(),
                    comment.getParentComment() == null ? null : comment.getParentComment().getId()
            );
        });
    }

    @Test
    @DisplayName("작성자는 자신의 댓글을 지울 수 있어야 합니다")
    void authorCanDeleteOwnComment() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        for (Comment comment : dataDTO.getComments()) {
            commentService.deleteComment(comment.getAuthor().getId(), comment.getId());
            em.flush();
            em.clear();
            Comment findComment = commentService.commentWithAuthor(comment.getId());
            assertThat(findComment.isDeleted()).isTrue();
        }
    }

    @Test
    @DisplayName("관리자는 모든 댓글을 지울 수 있어야 합니다")
    void adminCanDeleteAllComment() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Member admin = dataDTO.adminMember();
        for (Comment comment : dataDTO.getComments()) {
            commentService.deleteComment(admin.getId(), comment.getId());
            em.flush();
            em.clear();
            Comment findComment = commentService.commentWithAuthor(comment.getId());
            assertThat(findComment.isDeleted()).isTrue();
        }
    }

    @Test
    @DisplayName("관리자는 모든 댓글을 지울 수 있어야 합니다")
    void memberCanNotDeleteOtherMembersComment() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        for (Comment comment : dataDTO.getComments()) {
            Member anotherMember = dataDTO.anotherMember(comment.getAuthor());
            assertThatThrownBy(() -> commentService.deleteComment(anotherMember.getId(), comment.getId()))
                    .isInstanceOf(CommentDeleteFailedException.class);
            em.flush();
            em.clear();
            Comment findComment = commentService.commentWithAuthor(comment.getId());
            assertThat(findComment.isDeleted()).isFalse();
        }
    }
}
