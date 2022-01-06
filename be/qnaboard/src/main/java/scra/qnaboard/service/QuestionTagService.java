package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.domain.repository.tag.QuestionTagRepository;
import scra.qnaboard.domain.repository.tag.QuestionTagSimpleQueryRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.domain.repository.tag.TagSimpleQueryRepository;
import scra.qnaboard.service.exception.tag.delete.TagDeleteFailedException;
import scra.qnaboard.service.exception.tag.edit.UnauthorizedTagEditException;
import scra.qnaboard.service.exception.tag.search.TagNotFoundException;
import scra.qnaboard.web.dto.tag.list.TagDTO;
import scra.qnaboard.web.dto.tag.list.TagListDTO;
import scra.qnaboard.web.dto.tag.search.TagSearchResultDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionTagService {

    private final MemberService memberService;
    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;
    private final QuestionTagSimpleQueryRepository questionTagSimpleQueryRepository;
    private final TagSimpleQueryRepository tagSimpleQueryRepository;

    public void createQuestionTags(Question question, List<Long> tagIds) {
        List<Tag> tags = tagSimpleQueryRepository.tagsByIdIn(tagIds);

        List<QuestionTag> questionTags = tags.stream()
                .map(tag -> new QuestionTag(tag, question))
                .collect(Collectors.toList());

        questionTagRepository.saveAll(questionTags);
    }

    @Transactional
    public void updateQuestionTags(Question question, List<Long> tagIds) {
        questionTagSimpleQueryRepository.deleteByQuestionId(question.getId());
        createQuestionTags(question, tagIds);
    }
}
