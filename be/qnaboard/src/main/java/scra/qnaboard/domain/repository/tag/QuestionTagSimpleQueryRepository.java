package scra.qnaboard.domain.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.domain.entity.questiontag.QuestionTagId;

import java.util.List;

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

    public void deleteByQuestionId(long questionId) {
        queryFactory.delete(questionTag)
                .where(questionTag.id.questionId.eq(questionId))
                .execute();
    }

    public void deleteByQuestionTagIdIn(List<QuestionTagId> questionTagIds) {
        queryFactory.delete(questionTag)
                .where(questionTag.id.in(questionTagIds))
                .execute();
    }

    public List<QuestionTag> questionTagByQuestionId(long questionId) {
        return queryFactory.selectFrom(questionTag)
                .where(questionTag.id.questionId.eq(questionId))
                .fetch();
    }
}
