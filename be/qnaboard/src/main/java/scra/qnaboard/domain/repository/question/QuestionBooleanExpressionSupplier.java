package scra.qnaboard.domain.repository.question;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static scra.qnaboard.domain.entity.post.QQuestion.question;

@Component
public class QuestionBooleanExpressionSupplier {

    /**
     * soft delete 되지 않았고 아이디가 같은지에 대한 BooleanExpression. 거의 모든 단건 검색 쿼리에서 사용한다
     * @param questionId 조회할 질문게시글 아이디
     * @return where절에 들어갈 조건
     */
    public BooleanExpression notDeletedAndEqualsId(long questionId) {
        return question.id.eq(questionId).and(question.deleted.isFalse());
    }
}
