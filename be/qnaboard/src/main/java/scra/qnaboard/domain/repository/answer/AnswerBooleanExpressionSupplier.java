package scra.qnaboard.domain.repository.answer;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;
import scra.qnaboard.domain.entity.post.QQuestion;

import static scra.qnaboard.domain.entity.post.QAnswer.answer;
import static scra.qnaboard.domain.entity.post.QQuestion.question;

@Component
public class AnswerBooleanExpressionSupplier {

    /**
     * soft delete 되지 않았고 질문게시글의 아이디가 같은지에 대한 BooleanExpression.
     *
     * @param questionId 조회할 질문게시글 아이디
     * @return where절에 들어갈 조건
     */
    public BooleanExpression answerNotDeletedAndEqualsQuestionId(long questionId) {
        return answer.question.id.eq(questionId).and(answer.deleted.isFalse());
    }

    /**
     * 위의 메서드와 같은 일을 하긴 하는데, 질문글 아이디를 파라미터로 받지 않는다.
     * 서브쿼리에서 사용한다.
     */
    public BooleanExpression answerNotDeletedAndEqualsQuestionId() {
        return answer.question.id.eq(question.id).and(answer.deleted.isFalse());
    }
}
