package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.domain.repository.vote.VoteRepository;
import scra.qnaboard.domain.repository.vote.VoteSimpleQueryRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteSimpleQueryRepository voteSimpleQueryRepository;
    private final MemberService memberService;
    private final PostService postService;

    public long voteScore(long postId) {
        return voteSimpleQueryRepository.voteScore(postId);
    }

    public Map<Long, Long> voteScoreByPostIdList(List<Long> postIdList) {
        return voteSimpleQueryRepository.voteScoreByPostIdList(postIdList);
    }

    @Transactional
    public void voteUp(Long requesterId, Long postId) {
        vote(requesterId, postId, VoteType.UP);
    }

    @Transactional
    public void voteDown(Long requesterId, Long postId) {
        vote(requesterId, postId, VoteType.DOWN);
    }

    @Transactional
    public void vote(Long requesterId, Long postId, VoteType voteType) {
        //투표자와 대상 게시글 검색
        Member member = memberService.findMember(requesterId);
        Post post = postService.findPostById(postId);

        //이미 투표했는지 확인하기 위해 투표 엔티티 검색
        Optional<Vote> optionalVote = voteSimpleQueryRepository.findById(member, post);

        //투표 저장
        saveVote(optionalVote, member, post, voteType);
    }

    private void saveVote(Optional<Vote> optionalVote, Member member, Post post, VoteType voteType) {
        if (optionalVote.isPresent()) {
            //투표한 적이 있다면, 기존 투표를 업데이트한다
            Vote findVote = optionalVote.get();
            findVote.changeVoteType(voteType);
        } else {
            //투표한 적이 없다면 새로운 투표정보를 생성한다
            Vote newVote = new Vote(member, post, voteType);
            voteRepository.save(newVote);
        }
    }
}
