package scra.qnaboard.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.QQuestionTag;
import scra.qnaboard.domain.entity.post.QAnswer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.service.exception.QuestionNotFoundException;
import scra.qnaboard.web.dto.question.detail.AnswerDTO;
import scra.qnaboard.web.dto.question.detail.QAnswerDTO;
import scra.qnaboard.web.dto.question.detail.QQuestionDetailDTO;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.web.dto.question.list.QQuestionSummaryDTO;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;
import scra.qnaboard.web.dto.tag.QTagDTO;
import scra.qnaboard.web.dto.tag.TagDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class QuestionSearchRepository {

    private final JPAQueryFactory queryFactory;

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
                        question.answers.size(),
                        question.viewCount,
                        question.createdDate,
                        question.author.nickname
                )).from(question)
                .innerJoin(question.author, member)
                .fetch();

        //2. 연관된 태그정보 조회쿼리의 In절에서 사용할 ID 컬렉션을 스트림으로 생성한다
        List<Long> questionIds = questions.stream()
                .map(QuestionSummaryDTO::getQuestionId)
                .collect(Collectors.toList());

        //3. 질문목록에서 참조하는 태그정보 조회(QuestionTag와 Tag까지 조인해서 가져오되, in 절을 사용해서 최적화함)
        List<TagDTO> tags = queryFactory
                .select(new QTagDTO(
                        tag.id,
                        questionTag.question.id,
                        tag.name
                )).from(questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(questionTag.question.id.in(questionIds))
                .fetch();

        //4. 태그의 Question ID값을 가지고 Map으로 그룹화 함
        Map<Long, List<TagDTO>> tagMap = tags.stream()
                .collect(Collectors.groupingBy(TagDTO::getQuestionId));

        //5. 4번에서 만든 맵을 가지고 Question DTO에 태그정보를 입력함
        questions.forEach(question -> question.setTags(tagMap.get(question.getQuestionId())));

        return questions;
    }

    /**
     * 패치조인으로 질문글 상세보기를 하는 메서드
     *
     * @param questionId 상세보기 대상 질문글의 아이디
     * @return 질문글 엔티티를 감싼 옵셔널 객체
     */
    public Optional<Question> questionDetail(long questionId) {
        Question findQuestion = queryFactory
                .select(question).distinct()
                .from(question)
                .innerJoin(question.author, member).fetchJoin()
                .leftJoin(question.answers, answer).fetchJoin()
                .leftJoin(answer.author, member).fetchJoin()
                .where(question.id.eq(questionId))
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .fetchOne();

        queryFactory.select(question).distinct()
                .from(question)
                .leftJoin(question.questionTags, questionTag).fetchJoin()
                .leftJoin(questionTag.tag, tag).fetchJoin()
                .where(question.id.eq(questionId))
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .fetch();

        return Optional.ofNullable(findQuestion);
    }

    /**
     * DTO 프로젝션으로 질문글 상세보기를 하는 메서드
     *
     * @param questionId 상세보기 대상 질문글의 아이디
     * @return 질문글 DTO를 감싼 옵셔널 객체
     */
    public QuestionDetailDTO questionDetailV2(long questionId) {
        QuestionDetailDTO detailDTO = queryFactory
                .select(new QQuestionDetailDTO(
                        question.id,
                        question.title,
                        question.content,
                        question.upVoteCount.subtract(question.downVoteCount),
                        question.viewCount,
                        question.createdDate,
                        question.lastModifiedDate,
                        question.author.id,
                        question.author.nickname
                )).from(question)
                .innerJoin(question.author, member)
                .where(question.id.eq(questionId))
                .fetchOne();

        List<TagDTO> tags = queryFactory
                .select(new QTagDTO(
                        tag.id,
                        questionTag.question.id,
                        tag.name
                )).from(questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(questionTag.question.id.eq(questionId))
                .fetch();

        List<AnswerDTO> answers = queryFactory
                .select(new QAnswerDTO(
                        answer.id,
                        answer.content,
                        answer.upVoteCount.subtract(answer.downVoteCount),
                        answer.createdDate,
                        answer.lastModifiedDate,
                        answer.author.id,
                        answer.author.nickname
                )).from(answer)
                .innerJoin(answer.author, member)
                .where(answer.question.id.eq(questionId))
                .fetch();

        if (detailDTO == null) {
            throw new QuestionNotFoundException(questionId);
        }

        detailDTO.updateDependency(tags, answers);

        return detailDTO;
    }
}
