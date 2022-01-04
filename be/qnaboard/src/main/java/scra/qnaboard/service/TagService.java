package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.domain.repository.tag.TagSimpleQueryRepository;
import scra.qnaboard.service.exception.tag.delete.TagDeleteFailedException;
import scra.qnaboard.service.exception.tag.edit.UnauthorizedTagEditException;
import scra.qnaboard.service.exception.tag.search.TagNotFoundException;
import scra.qnaboard.web.dto.tag.list.TagDTO;
import scra.qnaboard.web.dto.tag.list.TagListDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final MemberService memberService;
    private final TagRepository tagRepository;
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
    public void editTag(long requesterId, long tagId, String name) {
        Tag tag = tagWithAuthor(tagId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면 실패해야함
        if (requester.isNotAdmin()) {
            throw new UnauthorizedTagEditException(tagId, requesterId);
        }

        tag.update(name);
    }

    @Transactional
    public void deleteTag(long requesterId, long tagId) {
        Tag tag = tagWithAuthor(tagId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면 실패해야함
        if (requester.isNotAdmin()) {
            throw new TagDeleteFailedException(TagDeleteFailedException.UNAUTHORIZED, tagId, requesterId);
        }

        tag.delete();
    }

    public Tag tagWithAuthor(long tagId) {
        return tagSimpleQueryRepository.tagWithAuthor(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
    }

}
