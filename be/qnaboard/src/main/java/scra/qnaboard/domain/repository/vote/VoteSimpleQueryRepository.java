package scra.qnaboard.domain.repository.vote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.vote.Vote;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.vote.QVote.vote;

@Repository
@RequiredArgsConstructor
public class VoteSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Vote> findById(Member member, Post post) {
        Vote findVote = queryFactory.selectFrom(vote)
                .where(vote.member.id.eq(member.getId())
                        .and(vote.post.id.eq(post.getId())))
                .fetchOne();

        return Optional.ofNullable(findVote);
    }

    public long voteScore(long postId) {
        List<VoteResultDTO> voteCountResult = queryFactory
                .select(new QVoteResultDTO(
                        vote.post.id,
                        vote.count(),
                        vote.voteType
                )).from(vote)
                .where(vote.post.id.eq(postId))
                .groupBy(vote.voteType)
                .fetch();

        return VoteResultDTO.countVoteScore(voteCountResult);
    }

    public Map<Long, Long> voteScoreByPostIdList(List<Long> postId) {
        List<VoteResultDTO> voteCountResult = queryFactory
                .select(new QVoteResultDTO(
                        vote.post.id,
                        vote.count(),
                        vote.voteType
                )).from(vote)
                .where(vote.post.id.in(postId))
                .groupBy(vote.post.id, vote.voteType)
                .fetch();

        return voteCountResult.stream()
                .collect(Collectors.groupingBy(VoteResultDTO::getPostId))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> VoteResultDTO.countVoteScore(v.getValue())));
    }
}
