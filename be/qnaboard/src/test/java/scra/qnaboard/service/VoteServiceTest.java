package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.service.exception.vote.DuplicateVoteException;
import scra.qnaboard.utils.QueryUtils;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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

        testTemplate(questions, members, this::doUpVote, this::testVoteCreation);
    }

    private void testVoteCreation(Question question, Member member) {
        Long memberId = member.getId();
        Long postId = question.getId();

        Vote findVote = QueryUtils.findVoteById(em, memberId, postId);

        assertThat(findVote.getMember().getId()).isEqualTo(memberId);
        assertThat(findVote.getPost().getId()).isEqualTo(postId);
    }

    @Test
    @DisplayName("중복투표하면 예외가 발생해야 함")
    void testDuplicatedVote() {
        TestDataDTO testDataDTO = TestDataInit.init(em);
        List<Question> questions = testDataDTO.getQuestions();
        List<Member> members = testDataDTO.getMembers();

        testTemplate(questions, members, this::doUpVote, this::testDuplicatedVote);
    }

    private void testDuplicatedVote(Question question, Member member) {
        assertThatThrownBy(() -> doUpVote(question, member)).isInstanceOf(DuplicateVoteException.class);
    }

    @Test
    @DisplayName("반대로 투표하는 것을 허용해야 함")
    void testReverseVote() {
        TestDataDTO testDataDTO = TestDataInit.init(em);
        List<Question> questions = testDataDTO.getQuestions();
        List<Member> members = testDataDTO.getMembers();

        testTemplate(questions, members, this::doUpVote, this::testReverseVote);
    }

    private void testReverseVote(Question question, Member member) {
        Vote findVote = QueryUtils.findVoteById(em, member.getId(), question.getId());
        VoteType reverseType = findVote.getVoteType() == VoteType.UP ? VoteType.DOWN : VoteType.UP;

        voteService.vote(member.getId(), question.getId(), reverseType);
        List<Vote> allVoteById = QueryUtils.findAllVoteById(em, member.getId(), question.getId());
        assertThat(allVoteById.size()).isEqualTo(2);
    }

//    @Test
//    @DisplayName("단일 게시글의 투표점수를 조회할 수 있어야 함")
//    void testVoteScoreOfSinglePost() {
//        TestDataDTO testDataDTO = TestDataInit.init(em);
//
//        List<Member> members = testDataDTO.getMembers();
//        Question question = testDataDTO.question();
//
//        testVoteScoreOfSinglePost(members, question);
//    }

//    private void testVoteScoreOfSinglePost(List<Member> members, Question question) {
//        //모든 멤버가 upvote 한 다음 투표점수 집계
//        members.forEach(member -> doUpVote(question, member));
////        long voteScore1 = voteService.voteScore(question.getId());
//
//        //모든 멤버가 upvote했으니 점수는 멤버의 인원수만큼 나와야 함
//        assertThat(voteScore1).isEqualTo(members.size());
//
//        //모든 멤버가 downvote 한 다음 투표점수 집계
//        members.forEach(member -> doDownVote(question, member));
//        long voteScore2 = voteService.voteScore(question.getId());
//
//        //모두가 downvote했으니 (-인원수)가 되어야 함(0이 아닌 이유는, up으로 투표했던 애들이 down으로 바꿔서 그렇다)
//        assertThat(voteScore2).isEqualTo(0);
//    }

//    @Test
//    @DisplayName("여러 게시글의 투표점수를 조회할 수 있어야 함")
//    void testVoteScoreOfMultiplePost() {
//        TestDataDTO testDataDTO = TestDataInit.init(em);
//
//        List<Member> members = testDataDTO.getMembers();
//        List<Question> questions = testDataDTO.getQuestions();
//
//        for (Question question : questions) {
//            members.forEach(member -> doUpVote(question, member));
//        }
//
//        List<Long> questionIds = questions.stream()
//                .map(Question::getId)
//                .collect(Collectors.toList());
//
////        Map<Long, Long> voteScoreMap = voteService.voteScoreByPostIdList(questionIds);
//
//        int numberOfMembers = members.size();
//
////        for (Question question : questions) {
////            assertThat(voteScoreMap.get(question.getId())).isEqualTo(numberOfMembers);
////        }
//    }

    private void testTemplate(List<Question> questions,
                              List<Member> members,
                              BiConsumer<Question, Member> peekBehavior,
                              BiConsumer<Question, Member> testBehavior) {
        for (Question question : questions) {
            members.stream()
                    .peek(member -> peekBehavior.accept(question, member))
                    .forEach(member -> testBehavior.accept(question, member));
        }
    }

    private void doUpVote(Question question, Member member) {
        voteService.voteUp(member.getId(), question.getId());
    }

    private void doDownVote(Question question, Member member) {
        voteService.voteDown(member.getId(), question.getId());
    }
}
