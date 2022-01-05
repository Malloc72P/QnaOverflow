package scra.qnaboard.domain.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.QuestionTag;

import java.util.List;

import static scra.qnaboard.domain.entity.QQuestionTag.questionTag;

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
