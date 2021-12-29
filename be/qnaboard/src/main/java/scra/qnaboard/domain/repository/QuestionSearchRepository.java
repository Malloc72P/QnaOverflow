package scra.qnaboard.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.service.exception.QuestionNotFoundException;
import scra.qnaboard.web.dto.question.detail.*;
import scra.qnaboard.web.dto.question.list.QQuestionSummaryDTO;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;
import scra.qnaboard.web.dto.tag.QTagDTO;
import scra.qnaboard.web.dto.tag.TagDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.QComment.comment;
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
        //1. 질문글 상세조회 - 질문글 작성자만 조인해서 가져옴
        QuestionDetailDTO detailDTO = questionDetailDtoByQuestionId(questionId);

        //2. 질문글태그(QuestionTag)랑 태그(Tag)만 가져옴
        List<TagDTO> tags = tagDtosByQuestionId(questionId);

        //3. 답변글과 답변글 작성자만 가져옴
        List<AnswerDTO> answers = answerDtosByQuestionId(questionId);

        //4. 대댓글 목록을 가져오기 위해 질문글과 답변글의 아이디를 컬렉션으로 모음
        List<Long> postIds = answers.stream()
                .map(AnswerDTO::getAnswerId)
                .collect(Collectors.toList());
        postIds.add(questionId);

        //5. 대댓글 조회
        List<CommentDTO> commentEntities = commentDtosByPostId(postIds);

        //6. 대댓글 조립 1 - 부모 게시글 아이디로 그룹화
        //originalCommentMap : 디비에서 가져온 댓글 목록을 게시글 ID로 그룹화해서 모아놓은 맵
        Map<Long, List<CommentDTO>> originalCommentMap = commentEntities.stream()
                .collect(Collectors.groupingBy(CommentDTO::getParentPostId));

        //7. 대댓글 조립 3 - 계층관계 조립
        //newCommentMap : 조립된 댓글목록을 적절한 게시글에 넣어주기 위한 맵 자료구조
        Map<Long, List<CommentDTO>> newCommentMap = new HashMap<>();
        for (Entry<Long, List<CommentDTO>> entry : originalCommentMap.entrySet()) {
            Long postId = entry.getKey();
            List<CommentDTO> originalComments = entry.getValue();
            List<CommentDTO> assembledComments = CommentDTO.assemble(originalComments);
            newCommentMap.put(postId, assembledComments);
        }

        //8. detailDTO의 부품 조립(태그목록, 답변게시글목록, 대댓글 목록)
        detailDTO.updateTags(tags);
        detailDTO.updateAnswer(answers);
        detailDTO.updateComments(newCommentMap.get(detailDTO.getQuestionId()));

        //9. 답변 게시글의 부품 조립(대댓글 목록)
        detailDTO.getAnswers()
                .forEach(answerDTO -> answerDTO.updateComments(newCommentMap.get(answerDTO.getAnswerId())));

        return detailDTO;
    }

    /**
     * 대댓글 DTO 리스트를 게시글 id로 조회
     *
     * @param postIds 게시글 id
     * @return 대댓글 DTO 리스트
     */
    private List<CommentDTO> commentDtosByPostId(List<Long> postIds) {
        return queryFactory
                .select(new QCommentDTO(
                        comment.id,
                        comment.author.id,
                        comment.author.nickname,
                        comment.createdDate,
                        comment.content,
                        comment.parentComment.id,
                        comment.parentPost.id
                )).from(comment)
                .innerJoin(comment.author, member)
                .where(comment.parentPost.id.in(postIds))
                .fetch();
    }

    /**
     * 답변게시글 DTO 리스트를 질문글 아이디로 조회
     *
     * @param questionId 질문글 아이디
     * @return 답변게시글 DTO 리스트
     */
    private List<AnswerDTO> answerDtosByQuestionId(long questionId) {
        return queryFactory
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
    }

    /**
     * 태그 DTO 리스트를 질문글 아이디로 조회
     *
     * @param questionId 질문글 아이디
     * @return 태그 DTO 리스트
     */
    private List<TagDTO> tagDtosByQuestionId(long questionId) {
        return queryFactory
                .select(new QTagDTO(
                        tag.id,
                        questionTag.question.id,
                        tag.name
                )).from(questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(questionTag.question.id.eq(questionId))
                .fetch();
    }

    /**
     * 질문글 상세보기 DTO를 질문글 아이디로 조회
     *
     * @param questionId 질문글 아이디
     * @return 질문글 상세보기 DTO
     */
    private QuestionDetailDTO questionDetailDtoByQuestionId(long questionId) {
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

        if (detailDTO == null) {
            throw new QuestionNotFoundException(questionId);
        }

        return detailDTO;
    }
}
