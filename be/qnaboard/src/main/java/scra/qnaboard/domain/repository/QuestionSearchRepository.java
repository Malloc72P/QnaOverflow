package scra.qnaboard.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.web.dto.question.list.QQuestionSummaryDTO;
import scra.qnaboard.web.dto.question.list.QTagDTO;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;
import scra.qnaboard.web.dto.question.list.TagDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.QMember.member;
import static scra.qnaboard.domain.entity.QQuestionTag.questionTag;
import static scra.qnaboard.domain.entity.QTag.tag;
import static scra.qnaboard.domain.entity.post.QQuestion.question;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionSearchRepository {

    private final JPAQueryFactory queryFactory;

    public List<QuestionSummaryDTO> search() {
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

        List<Long> questionIds = questions.stream()
                .map(QuestionSummaryDTO::getQuestionId)
                .collect(Collectors.toList());

        List<TagDTO> tags = queryFactory
                .select(new QTagDTO(
                        tag.id,
                        questionTag.question.id,
                        tag.name
                )).from(questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(questionTag.question.id.in(questionIds))
                .fetch();

        Map<Long, List<TagDTO>> tagMap = tags.stream()
                .collect(Collectors.groupingBy(TagDTO::getQuestionId));

        questions.forEach(question -> question.setTags(tagMap.get(question.getQuestionId())));

        return questions;
    }
}
