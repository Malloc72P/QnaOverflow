package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTagId;
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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final MemberService memberService;
    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;
    private final QuestionTagSimpleQueryRepository questionTagSimpleQueryRepository;
    private final TagSimpleQueryRepository tagSimpleQueryRepository;

    public TagListDTO tagList() {
        List<TagDTO> tags = tagSimpleQueryRepository.tagsWithAuthor()
                .stream()
                .map(TagDTO::from)
                .collect(Collectors.toList());

        return new TagListDTO(tags);
    }

    @Transactional
    public long createTag(long requesterId, String name, String description) {
        Member author = memberService.findMember(requesterId);

        Tag tag = new Tag(author, name, description);
        tagRepository.save(tag);

        return tag.getId();
    }

    @Transactional
    public void editTag(long requesterId, long tagId, String name, String description) {
        Tag tag = tagWithAuthor(tagId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면 실패해야함
        if (requester.isNotAdmin() && tag.isNotOwner(requester)) {
            throw new UnauthorizedTagEditException(tagId, requesterId);
        }

        tag.update(name, description);
    }

    @Transactional
    public void deleteTag(long requesterId, long tagId) {
        Tag tag = tagWithAuthor(tagId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면 실패해야함
        if (requester.isNotAdmin() && tag.isNotOwner(requester)) {
            throw new TagDeleteFailedException(TagDeleteFailedException.UNAUTHORIZED, tagId, requesterId);
        }

        tag.delete();
        questionTagSimpleQueryRepository.deleteByTagIdIn(tagId);
    }

    public TagDTO tagById(long tagId) {
        Tag findTag = tagWithAuthor(tagId);
        return TagDTO.from(findTag);
    }

    public TagSearchResultDTO search(String keyword) {
        List<Tag> tags = tagSimpleQueryRepository.searchTags(keyword);
        return TagSearchResultDTO.from(tags, keyword);
    }

    public List<Tag> tagByIdIn(List<Long> tagIds) {
        return tagSimpleQueryRepository.tagsByIdIn(tagIds);
    }

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

    private Tag tagWithAuthor(long tagId) {
        return tagSimpleQueryRepository.tagWithAuthor(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
    }

}
