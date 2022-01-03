package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;
import scra.qnaboard.web.dto.comment.CommentDTO;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EntityManager em;

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
}
