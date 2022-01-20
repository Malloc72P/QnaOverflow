package scra.qnaboard.web.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class TagApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    @WithMockUser
    void 태그_검색_API_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        String keyword = "tag-";
        String tagName = "tag-content-1";
        String tagDescription = "tag-content-1";
        //given
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tags.add(tagRepository.save(new Tag(author, tagName + i, tagDescription + i)));
        }

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/tags?keyword=" + keyword));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.tags", hasSize(3)));
    }
}
