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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Member member = createMember();

        Question question = createQuestion(member);

        Vote vote = new Vote(member, question, VoteType.UP);
        em.persist(vote);

        Optional<Vote> optionalVote = voteSimpleQueryRepository.findById(member, question);
        Vote findVote = optionalVote.orElse(null);

        assertThat(findVote).isNotNull()
                .isEqualTo(vote);
    }

//    @Test
//    @DisplayName("투표점수를 게시글 아이디 리스트로 가져올 수 있어야 함")
//    void testVoteScoreByPostIdList() {
//        Member member1 = createMember();
//        Member member2 = createMember();
//        Member member3 = createMember();
//
//        Question question1 = createQuestion(member1);
//
//        saveVote(member1, question1, VoteType.UP);
//        saveVote(member2, question1, VoteType.DOWN);
//        saveVote(member3, question1, VoteType.DOWN);
//
//        Question question2 = createQuestion(member1);
//
//        saveVote(member1, question2, VoteType.UP);
//        saveVote(member2, question2, VoteType.UP);
//        saveVote(member3, question2, VoteType.DOWN);
//
//        List<Long> postIds = new ArrayList<>();
//        postIds.add(question1.getId());
//        postIds.add(question2.getId());
//
//        Map<Long, Long> voteScoreMap = voteSimpleQueryRepository.voteScoreByPostIdList(postIds);
//        long voteScore1 = voteScoreMap.get(question1.getId());
//        long voteScore2 = voteScoreMap.get(question2.getId());
//
//        assertThat(voteScore1).isEqualTo(-1);
//        assertThat(voteScore2).isEqualTo(1);
//    }

    private Question createQuestion(Member member1) {
        Question question1 = new Question(member1, "content", "title");
        em.persist(question1);
        return question1;
    }

    private Member createMember() {
        Member member = new Member("member", MemberRole.NORMAL);
        em.persist(member);
        return member;
    }

    private void saveVote(Member member, Question question, VoteType voteType) {
        Vote vote = new Vote(member, question, voteType);
        em.persist(vote);
    }
}
