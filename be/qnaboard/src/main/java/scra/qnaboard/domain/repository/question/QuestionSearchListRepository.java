package scra.qnaboard.domain.repository.question;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.repository.answer.AnswerBooleanExpressionSupplier;
import scra.qnaboard.domain.repository.tag.QuestionTagQueryRepository;
import scra.qnaboard.dto.question.list.QQuestionSummaryDTO;
import scra.qnaboard.dto.question.list.QuestionSummaryDTO;
import scra.qnaboard.dto.question.search.ParsedSearchQuestionDTO;
import scra.qnaboard.dto.question.tag.QuestionTagDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.member.QMember.member;
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

    private final QuestionBooleanExpressionSupplier questionExpressions;
    private final AnswerBooleanExpressionSupplier answerExpressions;

    private final QuestionTagQueryRepository questionTagRepository;

    /**
     * 질문 목록을 검색어 DTO를 사용해서 조회함. <br>
     * 해당 기능을 위해 두번의 쿼리가 발생함. <br>
     * 1. 질문목록 조회쿼리(추가로 질문의 답글개수와 유저 이름을 같이 가져옴 ) <br>
     * 2. 연관된 태그조회 쿼리(QuestionTag와 Tag엔티티를 Join을 사용해서 가져옴.)
     *
     * @return 질문목록 DTO List
     */
    public Page<QuestionSummaryDTO> search(ParsedSearchQuestionDTO searchQuestionDTO, Pageable pageable) {
        //1. 질문목록 조회( 추가로 질문의 답글개수와 유저 이름을 같이 가져옴 )
        List<QuestionSummaryDTO> questions = queryFactory
                .select(new QQuestionSummaryDTO(
                        question.id,
                        question.title,
                        JPAExpressions.select(answer.id.count().intValue())
                                .from(answer)
                                .where(answerExpressions.answerNotDeletedAndEqualsQuestionId()),
                        question.viewCount,
                        question.score,
                        question.createdDate,
                        question.author.nickname
                )).from(question)
                .innerJoin(question.author, member)
                .where(questionExpressions.searchQuestions(searchQuestionDTO))
                .orderBy(question.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //2. 연관된 태그정보 조회쿼리의 In절에서 사용할 ID 컬렉션을 스트림으로 생성한다
        List<Long> questionIds = questions.stream()
                .map(QuestionSummaryDTO::getQuestionId)
                .collect(Collectors.toList());

        //3. 질문목록에서 참조하는 태그정보 조회(QuestionTag와 Tag까지 조인해서 가져오되, in 절을 사용해서 최적화함)
        List<QuestionTagDTO> tags = questionTagRepository.questionTagsBy(questionIds);

        //4. 태그의 Question ID값을 가지고 Map으로 그룹화 함
        Map<Long, List<QuestionTagDTO>> tagMap = tags.stream()
                .collect(Collectors.groupingBy(QuestionTagDTO::getQuestionId));

        //5. 태그정보 입력
        questions.forEach(question -> question.update(tagMap));

        return PageableExecutionUtils.getPage(questions, pageable, createCountQuery(searchQuestionDTO)::fetchOne);
    }

    private JPAQuery<Long> createCountQuery(ParsedSearchQuestionDTO searchQuestionDTO) {
        return queryFactory.select(question.id.count())
                .from(question)
                .where(questionExpressions.searchQuestions(searchQuestionDTO));
    }
}
