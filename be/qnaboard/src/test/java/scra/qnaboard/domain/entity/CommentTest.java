package scra.qnaboard.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.post.Question;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentTest {

    @Autowired
    private EntityManager em;

    /**
     * 저장된 엔티티는 영속성 컨택스트를 초기화 한 이후에 식별자를 가지고 찾을 수 있어야 하니까 이를 찾는 코드도 추가함. 또한 Auditing 기능이 잘 적용되고 있는지도 테스트해야함 <br>
     * 게시글을 가지고 대댓글을 전부 퍼올 수 있는지 확인하고, 가져온 대댓글의 부모가 정상적으로 매핑되어 있는지 검사한다
     */
    @Test
    @DisplayName("대댓글을 생성할 수 있어야 하며 질문글 엔티티로 연관된 모든 대댓글을 가지고 올 수 있어야 함")
    void testCommentOnQuestion() {
        Member member = new Member("member", MemberRole.USER);
        em.persist(member);

        Question question = new Question(member, "content1", "title");
        em.persist(question);

        testCommentsIsSaved(member, question);
    }

    @Test
    @DisplayName("대댓글을 생성할 수 있어야 하며 답변글 엔티티로 연관된 모든 대댓글을 가지고 올 수 있어야 함")
    void testCommentOnAnswer() {
        Member member = new Member("member", MemberRole.USER);
        em.persist(member);

        Question question = new Question(member, "content1", "title");
        em.persist(question);

        Answer answer = new Answer(member, "content1", question);
        em.persist(answer);

        testCommentsIsSaved(member, answer);
    }

    private void testCommentsIsSaved(Member author, Post post) {
        Comment c1 = new Comment(author, "c1", post, null);
        em.persist(c1);
        Comment c2 = new Comment(author, "c2", post, null);
        em.persist(c2);
        Comment c3 = new Comment(author, "c3", post, null);
        em.persist(c3);

        Comment c4 = new Comment(author, "c4", post, c1);
        em.persist(c4);

        Comment c5 = new Comment(author, "c5", post, c4);
        Comment c6 = new Comment(author, "c6", post, c4);
        em.persist(c5);
        em.persist(c6);

        em.flush();
        em.clear();

        Post foundPost = em.find(Post.class, post.getId());
        List<Comment> findComments = foundPost.getComments();
        assertThat(findComments).contains(c1, c2, c3, c4, c5, c6);

        Comment findC4 = em.find(Comment.class, c4.getId());
        assertThat(findC4.getParentComment()).isEqualTo(c1);

        Comment findC5 = em.find(Comment.class, c5.getId());
        Comment findC6 = em.find(Comment.class, c6.getId());
        assertThat(findC5.getParentComment()).isEqualTo(c4)
                .isEqualTo(findC6.getParentComment());

        assertThat(findC5.getCreatedDate())
                .isNotNull()
                .isEqualTo(findC5.getLastModifiedDate());

    }

}
