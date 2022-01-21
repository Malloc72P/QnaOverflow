package scra.qnaboard.domain.repository.vote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.vote.Vote;

import java.util.List;
import java.util.Optional;

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

    public List<Vote> findAllById(Member member, Post post) {
        return queryFactory.selectFrom(vote)
                .where(vote.member.id.eq(member.getId())
                        .and(vote.post.id.eq(post.getId())))
                .fetch();
    }

//    public long voteScore(long postId) {
//        //게시글 아이디로 투표 데이터를 전부 조회함
//        List<VoteResultDTO> voteCountResult = queryFactory
//                .select(new QVoteResultDTO(
//                        vote.post.id,
//                        vote.count(),
//                        vote.voteType
//                )).from(vote)
//                .where(vote.post.id.eq(postId))
//                .groupBy(vote.voteType)
//                .fetch();
//
//        //조회된 데이터를 가지고 투표점수를 계산함
//        return VoteResultDTO.countVoteScore(voteCountResult);
//    }

//    public Map<Long, Long> voteScoreByPostIdList(List<Long> postIdList) {
//        //게시글 아이디와 in절을 사용해서 투표 데이터를 전부 조회함
//        List<VoteResultDTO> voteCountResult = queryFactory
//                .select(new QVoteResultDTO(
//                        vote.post.id,
//                        vote.count(),
//                        vote.voteType
//                )).from(vote)
//                .where(vote.post.id.in(postIdList))
//                .groupBy(vote.post.id, vote.voteType)
//                .fetch();
//
//        //조회된 투표데이터를 가지고 투표점수를 계산한 다음, 맵에 저장함
//        //맵의 키는 게시글의 아이디이고, 값은 투표점수이다.
//        Map<Long, Long> voteScoreMap = voteCountResult.stream()
//                .collect(Collectors.groupingBy(VoteResultDTO::getPostId))
//                .entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, v -> VoteResultDTO.countVoteScore(v.getValue())));
//
//        //투표 정보가 아예 없는 게시글에 대한 예외처리
//        for (Long postId : postIdList) {
//            //투표가 하나도 없으면 0점으로 초기화함
//            if (!voteScoreMap.containsKey(postId)) {
//                voteScoreMap.put(postId, 0L);
//            }
//        }
//
//        return voteScoreMap;
//    }
}
