package scra.qnaboard.domain.repository.question;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import scra.qnaboard.domain.repository.answer.AnswerBooleanExpressionSupplier;
import scra.qnaboard.web.dto.question.search.ParsedSearchQuestionDTO;

import static scra.qnaboard.domain.entity.QTag.tag;
import static scra.qnaboard.domain.entity.post.QAnswer.answer;
import static scra.qnaboard.domain.entity.post.QQuestion.question;
import static scra.qnaboard.domain.entity.questiontag.QQuestionTag.questionTag;

@Component
@RequiredArgsConstructor
public class QuestionBooleanExpressionSupplier {

    private final AnswerBooleanExpressionSupplier answerExpressions;

    /**
     * soft delete 되지 않았고 아이디가 같은지에 대한 BooleanExpression. 거의 모든 단건 검색 쿼리에서 사용한다
     *
     * @param questionId 조회할 질문게시글 아이디
     * @return where절에 들어갈 조건
     */
    public BooleanExpression questionNotDeletedAndEqualsId(long questionId) {
        return question.id.eq(questionId).and(questionIsNotDeleted());
    }

    public BooleanBuilder searchQuestions(ParsedSearchQuestionDTO dto) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder
                .and(questionIsNotDeleted())
                .and(questionTitleLike(dto))
                .and(authorIdLike(dto))
                .and(answersCountGoe(dto))
                .and(scoreGoe(dto))
                .and(tagInRange(dto));
    }

    public BooleanExpression questionIsNotDeleted() {
        return question.deleted.isFalse();
    }

    public BooleanExpression questionTitleLike(ParsedSearchQuestionDTO dto) {
        return dto.hasTitle() ? question.title.like("%" + dto.getTitle() + "%") : null;
    }

    public BooleanExpression authorIdLike(ParsedSearchQuestionDTO dto) {
        return dto.hasAuthorId() ? question.author.id.eq(dto.getAuthorId()) : null;
    }

    public BooleanExpression answersCountGoe(ParsedSearchQuestionDTO dto) {
        return dto.hasAnswers() ? JPAExpressions.select(answer.count())
                .from(answer)
                .where(answerExpressions.answerNotDeletedAndEqualsQuestionId())
                .goe(dto.getAnswers())
                : null;
    }

    public BooleanExpression scoreGoe(ParsedSearchQuestionDTO dto) {
        return dto.hasScore() ? question.score.goe(dto.getScore()) : null;
    }

    public BooleanExpression tagInRange(ParsedSearchQuestionDTO dto) {
        return dto.hasTags() ? JPAExpressions.select(questionTag)
                .from(questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(questionTag.question.id.eq(question.id)
                        .and(tag.name.in(dto.getTags()))).exists()
                : null;
    }
}
