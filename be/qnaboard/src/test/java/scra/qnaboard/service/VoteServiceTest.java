package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.domain.repository.vote.VoteRepository;
import scra.qnaboard.domain.repository.vote.VoteSimpleQueryRepository;
import scra.qnaboard.service.exception.vote.DuplicateVoteException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteSimpleQueryRepository voteSimpleQueryRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private PostService postService;

    @Test
    void 게시글에_투표안했으면_성공해야함() {
        //given
        Member member = new Member("name", "email", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", 1L);
        //given
        Post post = new Question(member, "content-1", "title-1");
        ReflectionTestUtils.setField(post, "id", 2L);
        //given
        List<Vote> votes = new ArrayList<>();
        //given
        given(memberService.findMember(member.getId())).willReturn(member);
        given(postService.findPostById(post.getId())).willReturn(post);
        given(voteSimpleQueryRepository.findAllById(member, post)).willReturn(votes);

        //when &  then
        voteService.vote(member.getId(), post.getId(), VoteType.UP);
    }

    @Test
    void 게시글에_투표했더라도_중복투표아니면_성공해야함() {
        //given
        Member member = new Member("name", "email", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", 1L);
        //given
        Post post = new Question(member, "content-1", "title-1");
        ReflectionTestUtils.setField(post, "id", 2L);
        //given
        List<Vote> votes = new ArrayList<>();
        votes.add(new Vote(member, post, VoteType.UP));
        //given
        given(memberService.findMember(member.getId())).willReturn(member);
        given(postService.findPostById(post.getId())).willReturn(post);
        given(voteSimpleQueryRepository.findAllById(member, post)).willReturn(votes);

        //when &  then
        voteService.vote(member.getId(), post.getId(), VoteType.DOWN);
    }

    @Test
    void 게시글에_중복투표하는거면_실패해야함() {
        //given
        Member member = new Member("name", "email", MemberRole.USER);
        ReflectionTestUtils.setField(member, "id", 1L);
        //given
        Post post = new Question(member, "content-1", "title-1");
        ReflectionTestUtils.setField(post, "id", 2L);
        //given
        List<Vote> votes = new ArrayList<>();
        votes.add(new Vote(member, post, VoteType.UP));
        //given
        given(memberService.findMember(member.getId())).willReturn(member);
        given(postService.findPostById(post.getId())).willReturn(post);
        given(voteSimpleQueryRepository.findAllById(member, post)).willReturn(votes);

        //when &  then
        assertThatThrownBy(() -> voteService.vote(member.getId(), post.getId(), VoteType.UP))
                .isInstanceOf(DuplicateVoteException.class);
    }
}
