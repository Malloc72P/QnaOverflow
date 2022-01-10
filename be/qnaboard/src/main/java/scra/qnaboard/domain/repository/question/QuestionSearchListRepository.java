package scra.qnaboard.domain.repository.question;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.QTag;
import scra.qnaboard.domain.entity.questiontag.QQuestionTag;
import scra.qnaboard.domain.entity.vote.QVote;
import scra.qnaboard.domain.repository.tag.QuestionTagSimpleQueryRepository;
import scra.qnaboard.domain.repository.vote.VoteSimpleQueryRepository;
import scra.qnaboard.web.dto.question.list.QQuestionSummaryDTO;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;
import scra.qnaboard.web.dto.question.search.ParsedSearchQuestionDTO;
import scra.qnaboard.web.dto.question.tag.QuestionTagDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.QTag.tag;
import static scra.qnaboard.domain.entity.member.QMember.member;
import static scra.qnaboard.domain.entity.post.QAnswer.answer;
import static scra.qnaboard.domain.entity.post.QQuestion.question;
import static scra.qnaboard.domain.entity.questiontag.QQuestionTag.questionTag;
import static scra.qnaboard.domain.entity.vote.QVote.vote;

/**
 * 복잡한 동적쿼리 작성을 위해 QueryDSL을 사용하는 리포지토리
 */
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionSearchListRepository {
    private final JPAQueryFactory queryFactory;

    private final QuestionBooleanExpressionSupplier expressionSupplier;

    private final QuestionTagSimpleQueryRepository questionTagRepository;
    private final VoteSimpleQueryRepository voteRepository;

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
                        JPAExpressions.select(answer.id.count().intValue())
                                .from(answer)
                                .where(answer.question.id.eq(question.id)),
                        question.viewCount,
                        question.score,
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
        List<QuestionTagDTO> tags = questionTagRepository.questionTagsBy(questionIds);

        //4. 태그의 Question ID값을 가지고 Map으로 그룹화 함
        Map<Long, List<QuestionTagDTO>> tagMap = tags.stream()
                .collect(Collectors.groupingBy(QuestionTagDTO::getQuestionId));

        //5. 태그정보와 투표점수를 입력
        questions.forEach(question -> question.update(tagMap));

        return questions;
    }

    public List<QuestionSummaryDTO> search(ParsedSearchQuestionDTO searchQuestionDTO) {
        //1. 질문목록 조회( 추가로 질문의 답글개수와 유저 이름을 같이 가져옴 )
        List<QuestionSummaryDTO> questions = queryFactory
                .select(new QQuestionSummaryDTO(
                        question.id,
                        question.title,
                        JPAExpressions.select(answer.id.count().intValue())
                                .from(answer)
                                .where(answer.question.id.eq(question.id)),
                        question.viewCount,
                        question.score,
                        question.createdDate,
                        question.author.nickname
                )).from(question)
                .innerJoin(question.author, member)
                .where(question.deleted.isFalse()
                        .and(question.title.like("%" + searchQuestionDTO.getTitle() + "%"))
                        .and(question.author.id.eq(searchQuestionDTO.getAuthorId()))
                        .and(JPAExpressions.select(answer.count())
                                .from(answer)
                                .where(answer.question.id.eq(question.id)).goe(searchQuestionDTO.getAnswers()))
                        .and(question.score.goe(searchQuestionDTO.getScore()))
                        .and(JPAExpressions.select(questionTag)
                                .from(questionTag)
                                .innerJoin(questionTag.tag, tag)
                                .where(questionTag.question.id.eq(question.id)
                                        .and(tag.name.in(searchQuestionDTO.getTags()))).exists()
                        )
                )
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

        return questions;
    }
}
