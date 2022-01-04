package scra.qnaboard.domain.repository.question;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.service.exception.question.search.QuestionNotFoundException;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.answer.QAnswerDetailDTO;
import scra.qnaboard.web.dto.comment.CommentDTO;
import scra.qnaboard.web.dto.comment.QCommentDTO;
import scra.qnaboard.web.dto.question.detail.QQuestionDetailDTO;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.web.dto.question.tag.QQuestionTagDTO;
import scra.qnaboard.web.dto.question.tag.QuestionTagDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.QComment.comment;
import static scra.qnaboard.domain.entity.QMember.member;
import static scra.qnaboard.domain.entity.QQuestionTag.questionTag;
import static scra.qnaboard.domain.entity.QTag.tag;
import static scra.qnaboard.domain.entity.post.QAnswer.answer;
import static scra.qnaboard.domain.entity.post.QQuestion.question;

/**
 * 복잡한 단건조회는 여기서 작성함
 */
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionSearchDetailRepository {

    private final JPAQueryFactory queryFactory;

    private final QuestionBooleanExpressionSupplier expressionSupplier;

    /**
     * DTO 프로젝션으로 질문글 상세보기를 하는 메서드
     *
     * @param questionId 상세보기 대상 질문글의 아이디
     * @return 질문글 DTO를 감싼 옵셔널 객체
     */
    public QuestionDetailDTO questionDetail(long questionId) {
        //1. 질문글 상세조회 - 질문글 작성자만 조인해서 가져옴
        QuestionDetailDTO detailDTO = questionDetailDtoByQuestionId(questionId);

        //2. 질문글태그(QuestionTag)랑 태그(Tag)만 가져옴
        List<QuestionTagDTO> tags = tagDtosByQuestionId(questionId);

        //3. 답변글과 답변글 작성자만 가져옴
        List<AnswerDetailDTO> answers = answerDtosByQuestionId(questionId);

        //4. 대댓글 목록을 가져오기 위해 질문글과 답변글의 아이디를 컬렉션으로 모음
        List<Long> postIds = answers.stream()
                .map(AnswerDetailDTO::getAnswerId)
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
        List<CommentDTO> commentDTOS = queryFactory
                .select(new QCommentDTO(
                        comment.id,
                        comment.author.id,
                        comment.author.nickname,
                        comment.createdDate,
                        comment.content,
                        comment.parentComment.id,
                        comment.parentPost.id,
                        comment.deleted
                )).from(comment)
                .innerJoin(comment.author, member)
                .where(comment.parentPost.id.in(postIds))
                .fetch();

        //삭제된 코멘트는 블러 처리한다
        for (CommentDTO commentDTO : commentDTOS) {
            commentDTO.blur();
        }

        return commentDTOS;
    }

    /**
     * 답변게시글 DTO 리스트를 질문글 아이디로 조회
     *
     * @param questionId 질문글 아이디
     * @return 답변게시글 DTO 리스트
     */
    private List<AnswerDetailDTO> answerDtosByQuestionId(long questionId) {
        return queryFactory
                .select(new QAnswerDetailDTO(
                        answer.id,
                        answer.content,
                        answer.upVoteCount.subtract(answer.downVoteCount),
                        answer.createdDate,
                        answer.lastModifiedDate,
                        answer.author.id,
                        answer.author.nickname
                )).from(answer)
                .innerJoin(answer.author, member)
                .where(expressionSupplier.answerNotDeletedAndEqualsId(questionId))
                .fetch();
    }

    /**
     * 태그 DTO 리스트를 질문글 아이디로 조회
     *
     * @param questionId 질문글 아이디
     * @return 태그 DTO 리스트
     */
    private List<QuestionTagDTO> tagDtosByQuestionId(long questionId) {
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
                .where(expressionSupplier.questionNotDeletedAndEqualsId(questionId))
                .fetchOne();

        if (detailDTO == null) {
            throw new QuestionNotFoundException(questionId);
        }

        return detailDTO;
    }
}
