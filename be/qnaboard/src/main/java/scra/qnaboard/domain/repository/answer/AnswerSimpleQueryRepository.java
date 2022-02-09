package scra.qnaboard.domain.repository.answer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.QAnswer;
import scra.qnaboard.dto.answer.AnswerDetailDTO;
import scra.qnaboard.dto.answer.QAnswerDetailDTO;

import java.util.List;
import java.util.Optional;

import static scra.qnaboard.domain.entity.member.QMember.member;
import static scra.qnaboard.domain.entity.post.QAnswer.answer;


@Repository
@RequiredArgsConstructor
public class AnswerSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    private final AnswerBooleanExpressionSupplier expressionSupplier;

    /**
     * 답변글 아이디로 답변글과 작성자를 패치조인으로 가져와서 옵셔널로 감싸서 반환함
     */
    public Optional<Answer> answerWithAuthor(long answerId) {
        Answer answer = queryFactory.select(QAnswer.answer)
                .from(QAnswer.answer)
                .innerJoin(QAnswer.answer.author, member).fetchJoin()
                .where(QAnswer.answer.id.eq(answerId))
                .fetchOne();
        return Optional.ofNullable(answer);
    }

    /**
     * 답변게시글 DTO 리스트를 질문글 아이디로 조회 <br>
     * 투표점수는 조회하지 않는 점에 주의!
     *
     * @param questionId 질문글 아이디
     * @return 답변게시글 DTO 리스트
     */
    public List<AnswerDetailDTO> answerDtosByQuestionId(long questionId) {
        return queryFactory
                .select(new QAnswerDetailDTO(
                        answer.id,
                        answer.score,
                        answer.content,
                        answer.createdDate,
                        answer.lastModifiedDate,
                        answer.author.id,
                        answer.author.nickname
                )).from(answer)
                .innerJoin(answer.author, member)
                .where(expressionSupplier.answerNotDeletedAndEqualsQuestionId(questionId))
                .fetch();
    }

}
