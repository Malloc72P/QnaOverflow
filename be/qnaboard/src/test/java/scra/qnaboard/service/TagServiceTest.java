package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.repository.tag.QuestionTagSimpleQueryRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.domain.repository.tag.TagSimpleQueryRepository;
import scra.qnaboard.service.exception.tag.delete.TagDeleteFailedException;
import scra.qnaboard.service.exception.tag.edit.TagEditFailedException;
import scra.qnaboard.service.exception.tag.edit.UnauthorizedTagEditException;
import scra.qnaboard.web.dto.tag.search.TagSearchResultDTO;
import scra.qnaboard.web.dto.tag.search.TagSimpleDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private MemberService memberService;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private QuestionTagSimpleQueryRepository questionTagSimpleQueryRepository;
    @Mock
    private TagSimpleQueryRepository tagSimpleQueryRepository;

    @Test
    void 태그생성_테스트() throws Exception {
        //given
        long authorId = 1L;
        long tagId = 3L;
        String tagName = "tag-name";
        String tagDescription = "tag-desc";
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        Tag tagNoId = new Tag(author, tagName, tagDescription);
        Tag tag = new Tag(author, tagName, tagDescription);
        ReflectionTestUtils.setField(tag, "id", tagId);
        //given
        given(memberService.findMember(authorId)).willReturn(author);
        given(tagRepository.save(tagNoId)).willReturn(tag);

        //when
        long savedTagId = tagService.createTag(authorId, tagName, tagDescription);

        //then
        assertThat(savedTagId).isEqualTo(tagId);
    }

    @Test
    void 태그수정_테스트() throws Exception {
        //given
        long authorId = 1L;
        long tagId = 3L;
        String tagName = "tag-name";
        String tagDescription = "tag-desc";
        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-desc";
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        Tag tag = new Tag(author, tagName, tagDescription);
        ReflectionTestUtils.setField(tag, "id", tagId);
        //given
        given(memberService.findMember(authorId)).willReturn(author);
        given(tagSimpleQueryRepository.tagWithAuthor(tagId)).willReturn(Optional.of(tag));

        //when & then
        tagService.editTag(authorId, tagId, newTagName, newTagDescription);
    }

    @Test
    void 태그수정_실패_테스트_관리자및작성자아님() throws Exception {
        //given
        long authorId = 1L;
        long anotherAuthorId = 2L;
        long tagId = 3L;
        String tagName = "tag-name";
        String tagDescription = "tag-desc";
        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-desc";
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        Member anotherAuthor = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);
        //given
        Tag tag = new Tag(author, tagName, tagDescription);
        ReflectionTestUtils.setField(tag, "id", tagId);
        //given
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);
        given(tagSimpleQueryRepository.tagWithAuthor(tagId)).willReturn(Optional.of(tag));

        //when & then
        assertThatThrownBy(() -> tagService.editTag(anotherAuthorId, tagId, newTagName, newTagDescription))
                .isInstanceOf(UnauthorizedTagEditException.class)
                .isInstanceOf(TagEditFailedException.class);
    }

    @Test
    void 태그수정_테스트_관리자는성공해야함() throws Exception {
        //given
        long authorId = 1L;
        long anotherAuthorId = 2L;
        long tagId = 3L;
        String tagName = "tag-name";
        String tagDescription = "tag-desc";
        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-desc";
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        Member anotherAuthor = new Member("nickname", "email", MemberRole.ADMIN);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);
        //given
        Tag tag = new Tag(author, tagName, tagDescription);
        ReflectionTestUtils.setField(tag, "id", tagId);
        //given
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);
        given(tagSimpleQueryRepository.tagWithAuthor(tagId)).willReturn(Optional.of(tag));

        //when & then
        tagService.editTag(anotherAuthorId, tagId, newTagName, newTagDescription);
    }

    @Test
    void 태그삭제_테스트() throws Exception {
        //given
        long authorId = 1L;
        long anotherAuthorId = 1L;
        long tagId = 3L;
        String tagName = "tag-name";
        String tagDescription = "tag-desc";
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        Member anotherAuthor = new Member("nickname", "email", MemberRole.ADMIN);
        ReflectionTestUtils.setField(author, "id", anotherAuthorId);
        //given
        Tag tag = new Tag(author, tagName, tagDescription);
        ReflectionTestUtils.setField(tag, "id", tagId);
        //given
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);
        given(tagSimpleQueryRepository.tagWithAuthor(tagId)).willReturn(Optional.of(tag));

        //when & then
        tagService.deleteTag(authorId, tagId);
    }

    @Test
    void 태그삭제_실패_테스트_작성자및관리자아님() throws Exception {
        //given
        long authorId = 1L;
        long anotherAuthorId = 2L;
        long tagId = 3L;
        String tagName = "tag-name";
        String tagDescription = "tag-desc";
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        Member anotherAuthor = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);
        //given
        Tag tag = new Tag(author, tagName, tagDescription);
        ReflectionTestUtils.setField(tag, "id", tagId);
        //given
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);
        given(tagSimpleQueryRepository.tagWithAuthor(tagId)).willReturn(Optional.of(tag));

        //when & then
        assertThatThrownBy(() -> tagService.deleteTag(anotherAuthorId, tagId))
                .isInstanceOf(TagDeleteFailedException.class);
    }

    @Test
    void 태그삭제_테스트_관리자는성공해야함() throws Exception {
        //given
        long authorId = 1L;
        long anotherAuthorId = 2L;
        long tagId = 3L;
        String tagName = "tag-name";
        String tagDescription = "tag-desc";
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        Member anotherAuthor = new Member("nickname", "email", MemberRole.ADMIN);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);
        //given
        Tag tag = new Tag(author, tagName, tagDescription);
        ReflectionTestUtils.setField(tag, "id", tagId);
        //given
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);
        given(tagSimpleQueryRepository.tagWithAuthor(tagId)).willReturn(Optional.of(tag));

        //when & then
        tagService.deleteTag(anotherAuthorId, tagId);
    }

    @Test
    void 태그검색기능_테스트() throws Exception {
        //given
        long authorId = 1L;
        String keyword = "tag-";
        String tagName = "tag-name";
        String tagDescription = "tag-desc";
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Tag tag = new Tag(author, tagName + i, tagDescription + i);
            ReflectionTestUtils.setField(tag, "id", (long) i);
            tags.add(tag);
        }
        Map<Long, Tag> tagMap = tags.stream()
                .collect(Collectors.toMap(Tag::getId, Function.identity()));
        //given
        given(tagSimpleQueryRepository.searchTags(keyword)).willReturn(tags);

        //when
        TagSearchResultDTO searchResultDTO = tagService.search(keyword);

        //then
        assertThat(searchResultDTO.getTags().size()).isEqualTo(tags.size());
        assertThat(searchResultDTO.getSearchKeyword()).isEqualTo(keyword);
        for (TagSimpleDTO tag : searchResultDTO.getTags()) {
            Tag expected = tagMap.get(tag.getId());
            assertThat(tag.getName()).isEqualTo(expected.getName());
            assertThat(tag.getDescription()).isEqualTo(expected.getDescription());
        }
    }
}
