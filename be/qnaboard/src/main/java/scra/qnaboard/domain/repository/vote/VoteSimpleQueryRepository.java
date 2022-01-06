package scra.qnaboard.domain.repository.vote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.vote.QVote;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteId;

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
}
