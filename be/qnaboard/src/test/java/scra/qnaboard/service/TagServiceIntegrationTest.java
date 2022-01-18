package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.answer.AnswerRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.service.exception.tag.delete.TagDeleteFailedException;
import scra.qnaboard.service.exception.tag.edit.TagEditFailedException;
import scra.qnaboard.service.exception.tag.edit.UnauthorizedTagEditException;
import scra.qnaboard.web.dto.tag.search.TagSearchResultDTO;
import scra.qnaboard.web.dto.tag.search.TagSimpleDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class TagServiceIntegrationTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    void 태그_생성_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";

        //when
        long tagId = tagService.createTag(author.getId(), tagName, tagDescription);

        //then
        Tag tag = tagRepository.findById(tagId).orElse(null);
        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo(tagName);
        assertThat(tag.getDescription()).isEqualTo(tagDescription);
    }

    @Test
    void 태그_삭제_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";
        //given
        Tag tag = tagRepository.save(new Tag(author, tagName, tagDescription));

        //when
        tagService.deleteTag(author.getId(), tag.getId());

        //then
        Tag findTag = tagRepository.findById(tag.getId()).orElse(null);
        assertThat(tag).isNotNull();
        assertThat(tag.isDeleted()).isTrue();
    }

    @Test
    void 태그_삭제_실패_테스트_작성자및관리자아님() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";
        //given
        Tag tag = tagRepository.save(new Tag(author, tagName, tagDescription));

        //when & then
        assertThatThrownBy(() -> tagService.deleteTag(anotherAuthor.getId(), tag.getId()))
                .isInstanceOf(TagDeleteFailedException.class);

        //then
        Tag findTag = tagRepository.findById(tag.getId()).orElse(null);
        assertThat(tag).isNotNull();
        assertThat(tag.isDeleted()).isFalse();
    }

    @Test
    void 태그_삭제_테스트_관리자는성공해야함() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.ADMIN));
        //given
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";
        //given
        Tag tag = tagRepository.save(new Tag(author, tagName, tagDescription));

        //when & then
        tagService.deleteTag(anotherAuthor.getId(), tag.getId());

        //then
        Tag findTag = tagRepository.findById(tag.getId()).orElse(null);
        assertThat(tag).isNotNull();
        assertThat(tag.isDeleted()).isTrue();
    }

    @Test
    void 태그수정_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";
        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-desc";
        //given
        Tag tag = tagRepository.save(new Tag(author, tagName, tagDescription));

        //when
        tagService.editTag(author.getId(), tag.getId(), newTagName, newTagDescription);

        //then
        Tag findTag = tagRepository.findById(tag.getId()).orElse(null);
        assertThat(findTag).isNotNull();
        assertThat(findTag.getName()).isEqualTo(newTagName);
        assertThat(findTag.getDescription()).isEqualTo(newTagDescription);
    }

    @Test
    void 태그수정_실패_테스트_관리자및작성자아님() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";
        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-desc";
        //given
        Tag tag = tagRepository.save(new Tag(author, tagName, tagDescription));

        //when
        assertThatThrownBy(() -> tagService.editTag(anotherAuthor.getId(), tag.getId(), newTagName, newTagDescription))
                .isInstanceOf(TagEditFailedException.class)
                .isInstanceOf(UnauthorizedTagEditException.class);

        //then
        Tag findTag = tagRepository.findById(tag.getId()).orElse(null);
        assertThat(findTag).isNotNull();
        assertThat(findTag.getName()).isEqualTo(tagName);
        assertThat(findTag.getDescription()).isEqualTo(tagDescription);
    }

    @Test
    void 태그수정_테스트_관리자는성공해야함() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.ADMIN));
        //given
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";
        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-desc";
        //given
        Tag tag = tagRepository.save(new Tag(author, tagName, tagDescription));

        //when
        tagService.editTag(anotherAuthor.getId(), tag.getId(), newTagName, newTagDescription);

        //then
        Tag findTag = tagRepository.findById(tag.getId()).orElse(null);
        assertThat(findTag).isNotNull();
        assertThat(findTag.getName()).isEqualTo(newTagName);
        assertThat(findTag.getDescription()).isEqualTo(newTagDescription);
    }

    @Test
    void 태그검색기능_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.ADMIN));
        //given
        String keyword = "tag-";
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";
        //given
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Tag tag = new Tag(author, tagName + i, tagDescription + i);
            tagRepository.save(tag);
            tags.add(tag);
        }
        Map<Long, Tag> tagMap = tags.stream()
                .collect(Collectors.toMap(Tag::getId, Function.identity()));

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
