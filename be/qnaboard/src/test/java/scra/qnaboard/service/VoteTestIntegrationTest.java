package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.domain.repository.vote.VoteSimpleQueryRepository;
import scra.qnaboard.service.exception.vote.DuplicateVoteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class VoteTestIntegrationTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private VoteSimpleQueryRepository voteSimpleQueryRepository;

    @Test
    void 게시글에_투표안했으면_성공해야함() {
        //given
        Member member = memberRepository.save(new Member("name", "email", MemberRole.USER));
        //given
        Post post = questionRepository.save(new Question(member, "content-1", "title-1"));

        //when
        voteService.vote(member.getId(), post.getId(), VoteType.UP);

        //then
        List<Vote> votes = voteSimpleQueryRepository.findAllById(member, post);
        assertThat(votes.size()).isEqualTo(1);
        assertThat(votes.get(0)).extracting(
                Vote::getMember, Vote::getPost, Vote::getVoteType
        ).containsExactly(
                member, post, VoteType.UP
        );

    }

    @Test
    void 게시글에_투표했더라도_중복투표아니면_성공해야함() {
        //given
        Member member = memberRepository.save(new Member("name", "email", MemberRole.USER));
        //given
        Post post = questionRepository.save(new Question(member, "content-1", "title-1"));

        //when
        voteService.vote(member.getId(), post.getId(), VoteType.DOWN);
        voteService.vote(member.getId(), post.getId(), VoteType.UP);

        //then
        List<Vote> votes = voteSimpleQueryRepository.findAllById(member, post);
        assertThat(votes.size()).isEqualTo(2);
    }

    @Test
    void 게시글에_중복투표하는거면_실패해야함() {
        //given
        Member member = memberRepository.save(new Member("name", "email", MemberRole.USER));
        //given
        Post post = questionRepository.save(new Question(member, "content-1", "title-1"));

        //when
        voteService.vote(member.getId(), post.getId(), VoteType.DOWN);
        voteService.vote(member.getId(), post.getId(), VoteType.UP);
        assertThatThrownBy(() -> voteService.vote(member.getId(), post.getId(), VoteType.DOWN))
                .isInstanceOf(DuplicateVoteException.class);

        //then
        List<Vote> votes = voteSimpleQueryRepository.findAllById(member, post);
        assertThat(votes.size()).isEqualTo(2);
    }
}
