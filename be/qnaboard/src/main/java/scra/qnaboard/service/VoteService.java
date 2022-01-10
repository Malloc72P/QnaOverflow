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
import scra.qnaboard.service.exception.vote.DuplicateVoteException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteSimpleQueryRepository voteSimpleQueryRepository;
    private final MemberService memberService;
    private final PostService postService;

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
        List<Vote> votes = voteSimpleQueryRepository.findAllById(member, post);

        //투표 저장
        saveVote(votes, member, post, voteType);

        post.updateScore(voteType);
    }

    private void saveVote(List<Vote> votes, Member member, Post post, VoteType voteType) {
        //지금 하려는 투표유형과 동일한 투표가 이미 존재하는 경우
        Optional<Vote> alreadyExistVote = votes.stream()
                .filter(vote -> vote.isSameVote(voteType))
                .findAny();
        if (alreadyExistVote.isPresent()) {
            throw new DuplicateVoteException();
        }
        //존재하지 않으면 새로운 투표를 생성한다
        Vote newVote = new Vote(member, post, voteType);
        voteRepository.save(newVote);
    }
}
