package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.service.exception.tag.delete.TagDeleteFailedException;
import scra.qnaboard.service.exception.tag.edit.TagEditFailedException;
import scra.qnaboard.service.exception.tag.edit.UnauthorizedTagEditException;
import scra.qnaboard.utils.QueryUtils;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;
import scra.qnaboard.web.dto.tag.search.TagSearchResultDTO;
import scra.qnaboard.web.dto.tag.search.TagSimpleDTO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private TagService tagService;

    @Test
    @DisplayName("태그 서비스로 태그를 생성할 수 있어야 함")
    void testCreateTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Member member = dataDTO.noneAdminMember();
        String name = "test-name";
        String description = "test-description";

        long tagId = tagService.createTag(member.getId(), name, description);

        Tag findTag = QueryUtils.tagById(em, tagId);

        assertThat(findTag).extracting(
                Tag::getName, Tag::getDescription
        ).containsExactly(
                name, description
        );
    }

    @Test
    @DisplayName("관리자는 모든 태그를 삭제할 수 있어야 함")
    void testDeleteTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        Member admin = dataDTO.adminMember();
        Tag[] tags = dataDTO.getTags();

        for (Tag tag : tags) {
            tagService.deleteTag(admin.getId(), tag.getId());

            assertThat(QueryUtils.isDeletedTag(em, tag)).isTrue();
        }
    }

    @Test
    @DisplayName("작성자는 자신의 태그를 삭제할 수 있어야 함")
    void authorCanDeleteOwnTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        Tag[] tags = dataDTO.getTags();

        for (Tag tag : tags) {
            Member author = tag.getAuthor();
            tagService.deleteTag(author.getId(), tag.getId());

            assertThat(QueryUtils.isDeletedTag(em, tag)).isTrue();
        }
    }

    @Test
    @DisplayName("관리자가 아닌 사용자는 다른 이가 생성한 태그를 삭제할 수 없어야 함")
    void noneAdminMemberCanNotDeleteTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        Tag[] tags = dataDTO.getTags();

        for (Tag tag : tags) {
            Member member = dataDTO.anotherMemberAndNotAdmin(tag.getAuthor());
            assertThatThrownBy(() -> tagService.deleteTag(member.getId(), tag.getId()))
                    .isInstanceOf(TagDeleteFailedException.class);

            assertThat(QueryUtils.isDeletedTag(em, tag)).isFalse();
        }
    }

    @Test
    @DisplayName("관리자는 모든 태그를 수정할 수 있어야 함")
    void adminCanEditAllTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        Member adminMember = dataDTO.adminMember();
        Tag[] tags = dataDTO.getTags();

        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-description";

        for (Tag tag : tags) {
            tagService.editTag(adminMember.getId(), tag.getId(), newTagName, newTagDescription);
            Tag findTag = QueryUtils.tagById(em, tag.getId());

            assertThat(findTag.getName()).isEqualTo(newTagName);
            assertThat(findTag.getDescription()).isEqualTo(newTagDescription);
        }
    }

    @Test
    @DisplayName("작성자는 자신의 태그를 수정할 수 있어야 함")
    void authorCanEditOwnTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        Tag[] tags = dataDTO.getTags();

        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-description";

        for (Tag tag : tags) {
            Member author = tag.getAuthor();
            tagService.editTag(author.getId(), tag.getId(), newTagName, newTagDescription);
            Tag findTag = QueryUtils.tagById(em, tag.getId());

            assertThat(findTag.getName()).isEqualTo(newTagName);
            assertThat(findTag.getDescription()).isEqualTo(newTagDescription);
        }
    }

    @Test
    @DisplayName("일반 사용자는 타인이 작성한 태그를 수정할 수 없어야 함")
    void noneAdminCanNotEditTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        Tag[] tags = dataDTO.getTags();

        String newTagName = "new-tag-name";
        String newTagDescription = "new-tag-description";

        for (Tag tag : tags) {
            Member noneAdminMember = dataDTO.noneAdminMember();

            assertThatThrownBy(() -> tagService.editTag(noneAdminMember.getId(), tag.getId(), newTagName, newTagDescription))
                    .isInstanceOf(TagEditFailedException.class)
                    .isInstanceOf(UnauthorizedTagEditException.class);
            Tag findTag = QueryUtils.tagById(em, tag.getId());

            assertThat(findTag.getName()).isNotEqualTo(newTagName);
            assertThat(findTag.getDescription()).isNotEqualTo(newTagDescription);
        }

    }

    @Test
    @DisplayName("키워드로 태그를 검색할 수 있어야 함")
    void testSearchTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        Tag[] tags = dataDTO.getTags();

        String[] testcases = {
                "a", "b", "c", "d", "e"
        };

        for (String testcase : testcases) {
            TagSearchResultDTO searchResult = tagService.search(testcase);
            List<Tag> expectedResult = QueryUtils.tagByNameLike(em, testcase);

            assertThat(searchResult.getTags().size()).isEqualTo(expectedResult.size());

            List<String> searchResultNames = searchResult.getTags().stream()
                    .map(TagSimpleDTO::getName)
                    .sorted(String::compareTo)
                    .collect(Collectors.toList());

            List<String> realResultNames = expectedResult.stream()
                    .map(Tag::getName)
                    .sorted(String::compareTo)
                    .collect(Collectors.toList());

            assertThat(searchResultNames).isEqualTo(realResultNames);
        }
    }
}
