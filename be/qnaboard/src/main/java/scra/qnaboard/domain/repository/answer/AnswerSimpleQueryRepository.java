package scra.qnaboard.domain.repository.answer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.QAnswer;

import java.util.Optional;

import static scra.qnaboard.domain.entity.QMember.member;
import static scra.qnaboard.domain.entity.post.QAnswer.answer;

@Repository
@RequiredArgsConstructor
public class AnswerSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Answer> answerWithAuthor(long answerId) {
        Answer answer = queryFactory.select(QAnswer.answer)
                .from(QAnswer.answer)
                .innerJoin(QAnswer.answer.author, member).fetchJoin()
                .where(QAnswer.answer.id.eq(answerId))
                .fetchOne();
        return Optional.ofNullable(answer);
    }
}
