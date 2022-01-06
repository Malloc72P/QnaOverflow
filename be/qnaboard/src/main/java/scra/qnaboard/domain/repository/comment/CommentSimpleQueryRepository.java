package scra.qnaboard.domain.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.Comment;

import java.util.Optional;

import static scra.qnaboard.domain.entity.QComment.comment;
import static scra.qnaboard.domain.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class CommentSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Comment> commentWithAuthor(long commentId) {
        Comment findComment = queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.author, member).fetchJoin()
                .where(comment.id.eq(commentId))
                .fetchOne();
        return Optional.ofNullable(findComment);
    }
}
