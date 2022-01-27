package scra.qnaboard.domain.repository.tag;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

import static scra.qnaboard.domain.entity.QTag.tag;
import static scra.qnaboard.domain.entity.member.QMember.member;
import static scra.qnaboard.domain.entity.post.QQuestion.question;

@Repository
@RequiredArgsConstructor
public class TagSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Tag> tagsWithAuthor(Pageable pageable) {
        List<Tag> tags = queryFactory.select(tag)
                .from(tag)
                .where((tag.deleted.isFalse()))
                .innerJoin(tag.author, member).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(tags, pageable, tagsWithAuthorCountQuery()::fetchOne);
    }

    private JPAQuery<Long> tagsWithAuthorCountQuery() {
        return queryFactory.select(question.id.count())
                .from(question)
                .innerJoin(question.author, member);
    }

    public Optional<Tag> tagWithAuthor(long tagId) {
        Tag findTag = queryFactory
                .selectFrom(tag)
                .innerJoin(tag.author, member).fetchJoin()
                .where(tag.id.eq(tagId).and(tag.deleted.isFalse()))
                .fetchOne();

        return Optional.ofNullable(findTag);
    }

    public List<Tag> searchTags(String keyword) {
        return queryFactory.selectFrom(tag)
                .where(tag.name.like("%" + keyword + "%"))
                .fetch();
    }

    public List<Tag> tagsByIdIn(List<Long> tagIds) {
        return queryFactory.selectFrom(tag)
                .where(tag.id.in(tagIds))
                .fetch();
    }
}
