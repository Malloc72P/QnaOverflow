package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.service.exception.tag.delete.TagDeleteFailedException;
import scra.qnaboard.utils.QueryUtils;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;

import javax.persistence.EntityManager;

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
    @DisplayName("태그 서비스로 태그를 생성할 수 있어야 합니다")
    void testCreateTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);
        Member member = dataDTO.noneAdminMember();
        String name = "test-name";
        String description = "test-description";

        long tagId = tagService.createTag(member.getId(), name, description);

        Tag findTag = tagService.tagWithAuthor(tagId);

        assertThat(findTag).extracting(
                Tag::getName, Tag::getDescription
        ).containsExactly(
                name, description
        );
    }

    @Test
    @DisplayName("오직 관리자만이 태그를 삭제할 수 있습니다")
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
    @DisplayName("관리자가 아닌 사용자가 태그를 삭제하려고 하면 예외가 발생해야 합니다")
    void noneAdminMemberCanNotDeleteTag() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        Member noneAdminMember = dataDTO.noneAdminMember();
        Tag[] tags = dataDTO.getTags();

        for (Tag tag : tags) {
            assertThatThrownBy(() -> tagService.deleteTag(noneAdminMember.getId(), tag.getId()))
                    .isInstanceOf(TagDeleteFailedException.class);

            assertThat(QueryUtils.isDeletedTag(em, tag)).isFalse();
        }
    }
}
