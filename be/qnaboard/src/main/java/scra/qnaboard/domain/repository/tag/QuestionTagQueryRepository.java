package scra.qnaboard.domain.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.domain.entity.questiontag.QuestionTagId;
import scra.qnaboard.dto.question.tag.QQuestionTagDTO;
import scra.qnaboard.dto.question.tag.QuestionTagDTO;

import java.util.List;

import static scra.qnaboard.domain.entity.QTag.tag;
import static scra.qnaboard.domain.entity.questiontag.QQuestionTag.questionTag;

/**
 * QuestionTag에 관련된 약간 복잡한 쿼리를 위한 리포지토리
 * Question과 Tag의 다대다 관계 해소를 위한 중간 테이블에 해당하는 QuestionTag를 삭제하거나 조회한다
 */
@Repository
@RequiredArgsConstructor
public class QuestionTagQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 태그 아이디로 관련된 모든 QuestionTag를 삭제함
     */
    public void deleteByTagId(long tagId) {
        queryFactory.delete(questionTag)
                .where(questionTag.tag.id.eq(tagId))
                .execute();
    }

    /**
     * 질문글 아이디로 관련된 모든 QuestionTag를 삭제함
     * @param questionId
     */
    public void deleteByQuestionId(long questionId) {
        queryFactory.delete(questionTag)
                .where(questionTag.id.questionId.eq(questionId))
                .execute();
    }

    /**
     * QuestionTag 아이디 리스트로 In절을 활용해서 관련된 모든 QuestionTag를 삭제함
     */
    public void deleteByQuestionTagIdIn(List<QuestionTagId> questionTagIds) {
        queryFactory.delete(questionTag)
                .where(questionTag.id.in(questionTagIds))
                .execute();
    }

    /**
     * 질문글 아이디를 가지고 관련된 모든 QuestionTag를 조회해서 List로 반환함
     */
    public List<QuestionTag> questionTagByQuestionId(long questionId) {
        return queryFactory.selectFrom(questionTag)
                .where(questionTag.id.questionId.eq(questionId))
                .fetch();
    }

    /**
     * 질문글 아이디 리스트로 관련된 모든 QuestionTag를 조회해서 반환함
     * @param questionIds
     * @return
     */
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
