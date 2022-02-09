package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.repository.tag.QuestionTagSimpleQueryRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.domain.repository.tag.TagSimpleQueryRepository;
import scra.qnaboard.service.exception.tag.delete.UnauthorizedTagDeletionException;
import scra.qnaboard.service.exception.tag.edit.UnauthorizedTagEditException;
import scra.qnaboard.service.exception.tag.search.TagNotFoundException;
import scra.qnaboard.web.dto.tag.list.TagDTO;
import scra.qnaboard.web.dto.tag.search.TagSearchResultDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final MemberService memberService;
    private final TagRepository tagRepository;
    private final QuestionTagSimpleQueryRepository questionTagSimpleQueryRepository;
    private final TagSimpleQueryRepository tagSimpleQueryRepository;

    /**
     * 태그 검색 메서드
     *
     * @param keyword 검색 키워드
     * @return 태그검색정보를 담은 DTO
     */
    public TagSearchResultDTO search(String keyword) {
        List<Tag> tags = tagSimpleQueryRepository.searchTags(keyword);
        return TagSearchResultDTO.from(tags, keyword);
    }

    /**
     * 아이디로 태그 정보와 작성자 정보를 조회하는 메서드
     *
     * @param tagId 태그 아이디
     * @return 검색된 태그의 정보를 담은 DTO
     */
    public TagDTO tagById(long tagId) {
        Tag findTag = tagWithAuthor(tagId);
        return TagDTO.from(findTag);
    }

    /**
     * 태그 전체 목록 조회
     *
     * @return 태그 목록 DTO
     */
    public Page<TagDTO> tagList(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        //태그 엔티티를 조회하고, DTO로 변환한다
        return tagSimpleQueryRepository.tagsWithAuthor(pageRequest)
                .map(TagDTO::from);
    }

    /**
     * 태그 생성 메서드
     *
     * @param requesterId 요청자의 아이디
     * @param name        태그의 이름
     * @param description 태그에 대한 설명
     * @return 생성된 태그의 아이디
     */
    @Transactional
    public long createTag(long requesterId, String name, String description) {
        Member author = memberService.findMember(requesterId);

        Tag tag = new Tag(author, name, description);
        tag = tagRepository.save(tag);

        return tag.getId();
    }

    /**
     * 태그를 수정하는 메서드
     *
     * @param requesterId 요청자 아이디
     * @param tagId       수정대상 태그의 아이디
     * @param name        태그 이름
     * @param description 태그 설명
     */
    @Transactional
    public void editTag(long requesterId, long tagId, String name, String description) {
        //태그와 작성자정보를 조회함
        Tag tag = tagWithAuthor(tagId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면 실패해야함
        if (requester.isNotAdmin() && tag.isNotOwner(requester)) {
            throw new UnauthorizedTagEditException(tagId, requesterId);
        }

        //태그 수정
        tag.update(name, description);
    }

    /**
     * 태그 삭제 메서드
     *
     * @param requesterId 요청자 아이디
     * @param tagId       대상 태그 아이디
     */
    @Transactional
    public void deleteTag(long requesterId, long tagId) {
        //태그와 작성자정보를 조회함
        Tag tag = tagWithAuthor(tagId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면 실패해야함
        if (requester.isNotAdmin() && tag.isNotOwner(requester)) {
            throw new UnauthorizedTagDeletionException(tagId, requesterId);
        }

        tag.delete();
        questionTagSimpleQueryRepository.deleteByTagId(tagId);
    }

    /**
     * 태그와 작성자 정보를 함께 조회하는 메서드
     *
     * @param tagId 대상 태그의 아이디
     * @return 태그 엔티티
     */
    private Tag tagWithAuthor(long tagId) {
        return tagSimpleQueryRepository.tagWithAuthor(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
    }

}
