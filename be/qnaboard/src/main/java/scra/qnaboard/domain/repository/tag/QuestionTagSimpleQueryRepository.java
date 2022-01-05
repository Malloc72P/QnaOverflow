package scra.qnaboard.domain.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static scra.qnaboard.domain.entity.questiontag.QQuestionTag.questionTag;

@Repository
@RequiredArgsConstructor
public class QuestionTagSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public void deleteByTagIdIn(long tagId) {
        queryFactory.delete(questionTag)
                .where(questionTag.tag.id.eq(tagId))
                .execute();
    }
}
