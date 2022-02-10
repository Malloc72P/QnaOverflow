package scra.qnaboard.domain.repository.question;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.repository.answer.AnswerSimpleQueryRepository;
import scra.qnaboard.domain.repository.comment.CommentSimpleQueryRepository;
import scra.qnaboard.domain.repository.tag.QuestionTagQueryRepository;
import scra.qnaboard.service.exception.question.search.QuestionNotFoundException;
import scra.qnaboard.dto.answer.AnswerDetailDTO;
import scra.qnaboard.dto.comment.CommentDTO;
import scra.qnaboard.dto.question.detail.QQuestionDetailDTO;
import scra.qnaboard.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.dto.question.tag.QuestionTagDTO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.member.QMember.member;
import static scra.qnaboard.domain.entity.post.QQuestion.question;

/**
 * 복잡한 단건조회는 여기서 작성함
 */
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionSearchDetailRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    private final CommentSimpleQueryRepository commentRepository;
    private final AnswerSimpleQueryRepository answerRepository;
    private final QuestionTagQueryRepository questionTagRepository;

    private final QuestionBooleanExpressionSupplier expressionSupplier;

    /**
     * DTO 프로젝션으로 질문글 상세보기를 하는 메서드
     *
     * @param questionId 상세보기 대상 질문글의 아이디
     * @return 질문글 DTO를 감싼 옵셔널 객체
     */
    public QuestionDetailDTO questionDetail(long questionId) {
        //0. 조회수 올림
        increaseViewCount(questionId);

        //1. 질문글 상세조회 - 질문글 작성자만 조인해서 가져옴
        QuestionDetailDTO detailDTO = questionDetailDtoByQuestionId(questionId);

        //2. 질문글태그(QuestionTag)랑 태그(Tag)만 가져옴
        List<QuestionTagDTO> tags = questionTagRepository.tagDtosByQuestionId(questionId);

        //3. 답변글과 답변글 작성자만 가져옴
        List<AnswerDetailDTO> answers = answerRepository.answerDtosByQuestionId(questionId);

        //4. 대댓글 목록을 가져오기 위해 질문글과 답변글의 아이디를 컬렉션으로 모음
        List<Long> postIds = answers.stream()
                .map(AnswerDetailDTO::getAnswerId)
                .collect(Collectors.toList());
        postIds.add(questionId);

        //5. 대댓글 조회
        Map<Long, List<CommentDTO>> commentMap = commentRepository.commentMap(postIds);
        List<CommentDTO> questionComments = commentMap.get(questionId);

        //7. detailDTO의 부품 조립(태그목록, 답변게시글목록, 대댓글 목록)
        detailDTO.update(answers, tags, questionComments);

        //8. 답변 게시글의 부품 조립(대댓글 목록)
        detailDTO.getAnswers().forEach(answer -> answer.update(commentMap));

        entityManager.clear();
        entityManager.flush();

        return detailDTO;
    }

    private void increaseViewCount(long questionId) {
        queryFactory.update(question)
                .set(question.viewCount, question.viewCount.add(1))
                .where(question.id.eq(questionId))
                .execute();
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
                        question.viewCount,
                        question.score,
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
