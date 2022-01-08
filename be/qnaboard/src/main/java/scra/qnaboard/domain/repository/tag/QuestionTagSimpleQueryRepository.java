package scra.qnaboard.domain.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.domain.entity.questiontag.QuestionTagId;
import scra.qnaboard.web.dto.question.tag.QQuestionTagDTO;
import scra.qnaboard.web.dto.question.tag.QuestionTagDTO;

import java.util.List;

import static scra.qnaboard.domain.entity.QTag.tag;
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

    public List<QuestionTagDTO> questionTagsBy(List<Long> questionIds) {
        return queryFactory
                .select(new QQuestionTagDTO(
                        tag.id,
                        questionTag.question.id,
                        tag.name
                )).from(questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(questionTag.question.id.in(questionIds).and(tag.deleted.isFalse()))
                .fetch();
    }

    /**
     * 태그 DTO 리스트를 질문글 아이디로 조회
     *
     * @param questionId 질문글 아이디
     * @return 태그 DTO 리스트
     */
    public List<QuestionTagDTO> tagDtosByQuestionId(long questionId) {
        return queryFactory
                .select(new QQuestionTagDTO(
                        tag.id,
                        questionTag.question.id,
                        tag.name
                )).from(questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(questionTag.question.id.eq(questionId).and(tag.deleted.isFalse()))
                .fetch();
    }

}
