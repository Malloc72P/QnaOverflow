package scra.qnaboard.domain.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.QTag;
import scra.qnaboard.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

import static scra.qnaboard.domain.entity.QMember.member;
import static scra.qnaboard.domain.entity.QTag.tag;

@Repository
@RequiredArgsConstructor
public class TagSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tag> tagsWithAuthor() {
        return queryFactory.select(tag)
                .from(tag)
                .where((tag.deleted.isFalse()))
                .innerJoin(tag.author, member).fetchJoin()
                .fetch();
    }

    public Optional<Tag> tagWithAuthor(long tagId) {
        Tag findTag = queryFactory
                .selectFrom(tag)
                .innerJoin(tag.author, member).fetchJoin()
                .where(tag.id.eq(tagId).and(tag.deleted.isFalse()))
                .fetchOne();

        return Optional.ofNullable(findTag);
    }
}
