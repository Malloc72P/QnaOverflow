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

    /**
     * 질문글 검색에 사용되는 Where절을 만들어서 반환하는 메서드.
     * 검색 파라미터 DTO에 값이 있으면 Where절에 조건이 추가된다.
     * 반대로 값이 없는 경우, Where절에 조건을 추가하지 않는다.
     * @param dto 검색 파라미터 DTO. ParsedSearchQuestionDTO 클래스이다.
     * @return Where절에 해당하는 BooleanBuilder
     */
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

    /**
     * 삭제되지 않은 질문글에 대한 BooleanExpression
     */
    public BooleanExpression questionIsNotDeleted() {
        return question.deleted.isFalse();
    }

    /**
     * 제목이 검색파라미터와 비슷한지 확인하는 조건
     */
    public BooleanExpression questionTitleLike(ParsedSearchQuestionDTO dto) {
        return dto.hasTitle() ? question.title.like("%" + dto.getTitle() + "%") : null;
    }

    /**
     * 작성자의 아이디가 검색 파라미터와 같은지 확인하는 조건
     * @param dto
     * @return
     */
    public BooleanExpression authorIdLike(ParsedSearchQuestionDTO dto) {
        return dto.hasAuthorId() ? question.author.id.eq(dto.getAuthorId()) : null;
    }

    /**
     * 답변글 개수가 검색파라미터보다 이상인지 확인하는 조건
     */
    public BooleanExpression answersCountGoe(ParsedSearchQuestionDTO dto) {
        return dto.hasAnswers() ? JPAExpressions.select(answer.count())
                .from(answer)
                .where(answerExpressions.answerNotDeletedAndEqualsQuestionId())
                .goe(dto.getAnswers())
                : null;
    }

    /**
     * 질문글 추천점수가 검색파라미터보다 이상인지 확인하는 조건
     */
    public BooleanExpression scoreGoe(ParsedSearchQuestionDTO dto) {
        return dto.hasScore() ? question.score.goe(dto.getScore()) : null;
    }

    /**
     * 태그목록에 있는 모든 태그를 가지고 있는 질문글만 필터링함
     * 서브쿼리는 조건을 만족하는 태그의 개수를 반환함
     * 조건을 만족하는 태그의 개수(서브쿼리의 결과)는 검색 파라미터의 태그목록의 수와 같아야 함
     * 그래야 검색 파라미터의 모든 태그를 가지고 있는 질문글만 검색됨
     *
     * @param dto 검색 파라미터
     * @return 태그목록 검색필터
     */
    public BooleanExpression tagInRange(ParsedSearchQuestionDTO dto) {
        long tagSize = dto.getTags().size();
        return dto.hasTags() ?
                JPAExpressions.select(questionTag.count())
                        .from(questionTag)
                        .innerJoin(questionTag.tag, tag)
                        .where(questionTag.question.id.eq(question.id)
                                .and(tag.name.in(dto.getTags())))
                        .eq(tagSize)
                : null;
    }
}
