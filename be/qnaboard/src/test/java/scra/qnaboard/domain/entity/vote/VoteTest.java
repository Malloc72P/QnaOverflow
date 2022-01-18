package scra.qnaboard.domain.entity.vote;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class VoteTest {

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("투표 엔티티를 생성할 수 있어야 함")
    void testCreateVote() {
        Member member = new Member("member","email", MemberRole.USER);
        em.persist(member);

        Question question = new Question(member, "content", "title");
        em.persist(question);

        Vote vote = new Vote(member, question, VoteType.UP);
        em.persist(vote);

        Long memberId = vote.getMember().getId();
        Long postId = vote.getPost().getId();

        assertThat(memberId).isEqualTo(member.getId());
        assertThat(postId).isEqualTo(question.getId());
    }
}
