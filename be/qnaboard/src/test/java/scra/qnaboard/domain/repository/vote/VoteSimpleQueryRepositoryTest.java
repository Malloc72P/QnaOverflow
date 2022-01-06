package scra.qnaboard.domain.repository.vote;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteType;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class VoteSimpleQueryRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private VoteSimpleQueryRepository voteSimpleQueryRepository;

    @Test
    @DisplayName("투표 엔티티를 조회할 수 있어야 함")
    void testFindById() {
        Member member = new Member("member", MemberRole.NORMAL);
        em.persist(member);

        Question question = new Question(member, "content", "title");
        em.persist(question);

        Vote vote = new Vote(member, question, VoteType.UP);
        em.persist(vote);

        Optional<Vote> optionalVote = voteSimpleQueryRepository.findById(member, question);
        Vote findVote = optionalVote.orElse(null);

        assertThat(findVote).isNotNull()
                .isEqualTo(vote);
    }
}
