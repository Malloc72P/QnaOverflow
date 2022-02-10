package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.domain.repository.tag.QuestionTagRepository;
import scra.qnaboard.domain.repository.tag.QuestionTagQueryRepository;
import scra.qnaboard.domain.repository.tag.TagSimpleQueryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionTagService {

    private final QuestionTagRepository questionTagRepository;
    private final QuestionTagQueryRepository questionTagQueryRepository;
    private final TagSimpleQueryRepository tagSimpleQueryRepository;

    /**
     * 질문글에 여러 개의 태그를 추가하는 메서드
     *
     * @param question 태그를 추가할 질문글
     * @param tagIds   추가할 태그의 아이디 목록
     */
    @Transactional
    public void createQuestionTags(Question question, List<Long> tagIds) {
        List<Tag> tags = tagSimpleQueryRepository.tagsByIdIn(tagIds);

        List<QuestionTag> questionTags = tags.stream()
                .map(tag -> new QuestionTag(tag, question))
                .peek(question::addQuestionTag)
                .collect(Collectors.toList());

        questionTagRepository.saveAll(questionTags);
    }

    /**
     * 질문글의 태그정보를 수정하는 메서드 <br>
     * 기존에 적용된 태그정보를 전부 지우고 새로 입력한다
     *
     * @param question 대상 질문글의 아이디
     * @param tagIds   새로운 태그 정보(아이디 목록)
     */
    @Transactional
    public void updateQuestionTags(Question question, List<Long> tagIds) {
        questionTagQueryRepository.deleteByQuestionId(question.getId());
        question.resetTags();
        createQuestionTags(question, tagIds);
    }
}
