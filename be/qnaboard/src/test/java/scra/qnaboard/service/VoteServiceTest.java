package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteId;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.service.exception.vote.DuplicateVoteException;
import scra.qnaboard.utils.QueryUtils;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("up vote를 할 수 있어야 함")
    void testVoteCreation() {
        TestDataDTO testDataDTO = TestDataInit.init(em);
        List<Question> questions = testDataDTO.getQuestions();
        List<Member> members = testDataDTO.getMembers();

        testLayout(questions, members, this::doUpVote, this::testCreation);
    }

    private void testCreation(Question question, Member member) {
        Vote findVote = QueryUtils.findVoteById(em, member.getId(), question.getId());
        VoteId expectedId = new VoteId(member.getId(), question.getId());
        assertThat(findVote.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("중복투표하면 예외가 발생해야 함")
    void testDuplicatedVote() {
        TestDataDTO testDataDTO = TestDataInit.init(em);
        List<Question> questions = testDataDTO.getQuestions();
        List<Member> members = testDataDTO.getMembers();

        testLayout(questions, members, this::doUpVote, this::testDuplicatedVote);
    }

    private void testDuplicatedVote(Question question, Member member) {
        assertThatThrownBy(() -> doUpVote(question, member)).isInstanceOf(DuplicateVoteException.class);
    }

    @Test
    @DisplayName("반대로 투표하면 결과가 바뀌어야 함")
    void testReverseVote() {
        TestDataDTO testDataDTO = TestDataInit.init(em);
        List<Question> questions = testDataDTO.getQuestions();
        List<Member> members = testDataDTO.getMembers();

        testLayout(questions, members, this::doUpVote, this::testReverse);
    }

    private void testReverse(Question question, Member member) {
        Vote findVote = QueryUtils.findVoteById(em, member.getId(), question.getId());
        VoteType reverseType = findVote.getVoteType() == VoteType.UP ? VoteType.DOWN : VoteType.UP;

        voteService.castVote(member.getId(), question.getId(), reverseType);
        Vote result = QueryUtils.findVoteById(em, member.getId(), question.getId());
        assertThat(findVote.getVoteType()).isEqualTo(reverseType);
        assertThat(result.getVoteType()).isEqualTo(reverseType);

    }

    private void testLayout(List<Question> questions,
                            List<Member> members,
                            BiConsumer<Question, Member> peekBehavior,
                            BiConsumer<Question, Member> test) {
        for (Question question : questions) {
            members.stream()
                    .peek(member -> peekBehavior.accept(question, member))
                    .forEach(member -> test.accept(question, member));
        }
    }

    private void doUpVote(Question question, Member member) {
        voteService.castUpVote(member.getId(), question.getId());
    }
}
