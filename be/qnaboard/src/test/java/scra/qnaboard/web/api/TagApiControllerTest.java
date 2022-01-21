package scra.qnaboard.web.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import scra.qnaboard.configuration.auth.SecurityConfig;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.service.TagService;
import scra.qnaboard.web.dto.tag.search.TagSearchResultDTO;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = TagApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
class TagApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Test
    @WithMockUser
    void 태그_검색_API_테스트() throws Exception {
        //given
        Member member = new Member("member", "email", MemberRole.USER);
        //given
        String keyword = "tag-";
        //given
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Tag tag = new Tag(member, "tag-name", "tagdesc");
            ReflectionTestUtils.setField(tag, "id", (long) i);
            tags.add(tag);
        }
        TagSearchResultDTO tagSearchResultDTO = TagSearchResultDTO.from(tags, keyword);
        //given
        given(tagService.search(keyword)).willReturn(tagSearchResultDTO);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/tags?keyword=" + keyword)
                        .with(csrf())
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.searchKeyword", is(keyword)),
                        jsonPath("$.tags", hasSize(tags.size()))
                );
    }
}
