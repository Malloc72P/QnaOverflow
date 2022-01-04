package scra.qnaboard.domain.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.Tag;

import java.util.List;

import static scra.qnaboard.domain.entity.QMember.member;
import static scra.qnaboard.domain.entity.QTag.tag;

@Repository
@RequiredArgsConstructor
public class TagSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tag> tagsWithAuthor() {
        return queryFactory.select(tag)
                .from(tag)
                .innerJoin(tag.author, member).fetchJoin()
                .fetch();
    }
}
