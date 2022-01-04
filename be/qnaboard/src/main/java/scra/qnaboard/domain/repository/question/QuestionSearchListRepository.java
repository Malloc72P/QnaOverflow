package scra.qnaboard.domain.repository.question;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.web.dto.question.list.QQuestionSummaryDTO;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;
import scra.qnaboard.web.dto.question.tag.QQuestionTagDTO;
import scra.qnaboard.web.dto.question.tag.QuestionTagDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.QMember.member;
import static scra.qnaboard.domain.entity.QQuestionTag.questionTag;
import static scra.qnaboard.domain.entity.QTag.tag;
import static scra.qnaboard.domain.entity.post.QAnswer.answer;
import static scra.qnaboard.domain.entity.post.QQuestion.question;

/**
 * 복잡한 동적쿼리 작성을 위해 QueryDSL을 사용하는 리포지토리
 */
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionSearchListRepository {
    private final JPAQueryFactory queryFactory;

    private final QuestionBooleanExpressionSupplier expressionSupplier;

    /**
     * 질문 목록 조회 메서드 <br>
     * 원래는 복잡한 검색조건도 넣고 페이징 처리도 해야 하지만, 아직 적용하지 못함 <br>
     * 현재는 DTO 프로젝션을 사용해서 원하는 데이터를 퍼오기만 한다. <br>
     * 해당 기능을 위해 두번의 쿼리가 발생한다 <br>
     * 1. 질문목록 조회( 추가로 질문의 답글개수와 유저 이름을 같이 가져옴 ) <br>
     * 2. 질문목록에서 참조하는 태그정보 조회(QuestionTag와 Tag까지 조인해서 가져오되, in 절을 사용해서 최적화함)
     *
     * @return 질문목록 DTO List
     */
    public List<QuestionSummaryDTO> search() {
        //1. 질문목록 조회( 추가로 질문의 답글개수와 유저 이름을 같이 가져옴 )
        List<QuestionSummaryDTO> questions = queryFactory
                .select(new QQuestionSummaryDTO(
                        question.id,
                        question.title,
                        question.upVoteCount.subtract(question.downVoteCount),
                        JPAExpressions.select(answer.id.count().intValue())
                                .from(answer)
                                .where(answer.question.id.eq(question.id)),
                        question.viewCount,
                        question.createdDate,
                        question.author.nickname
                )).from(question)
                .innerJoin(question.author, member)
                .where(question.deleted.isFalse())
                .fetch();

        //2. 연관된 태그정보 조회쿼리의 In절에서 사용할 ID 컬렉션을 스트림으로 생성한다
        List<Long> questionIds = questions.stream()
                .map(QuestionSummaryDTO::getQuestionId)
                .collect(Collectors.toList());

        //3. 질문목록에서 참조하는 태그정보 조회(QuestionTag와 Tag까지 조인해서 가져오되, in 절을 사용해서 최적화함)
        List<QuestionTagDTO> tags = queryFactory
                .select(new QQuestionTagDTO(
                        tag.id,
                        questionTag.question.id,
                        tag.name
                )).from(questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(questionTag.question.id.in(questionIds).and(tag.deleted.isFalse()))
                .fetch();

        //4. 태그의 Question ID값을 가지고 Map으로 그룹화 함
        Map<Long, List<QuestionTagDTO>> tagMap = tags.stream()
                .collect(Collectors.groupingBy(QuestionTagDTO::getQuestionId));

        //5. 4번에서 만든 맵을 가지고 Question DTO에 태그정보를 입력함
        questions.forEach(question -> question.setTags(tagMap.get(question.getQuestionId())));

        return questions;
    }
}
