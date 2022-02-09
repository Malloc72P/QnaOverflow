package scra.qnaboard.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.dto.tag.search.TagSearchResultDTO;
import scra.qnaboard.dto.tag.search.TagSimpleDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class TagControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MessageSource message;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    @WithMockUser
    void 태그_검색_API_테스트() throws Exception {
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
        //then
        TagSearchResultDTO searchResultDTO = new ObjectMapper()
                .readValue(resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(), TagSearchResultDTO.class);
        assertThat(searchResultDTO.getTags().size()).isEqualTo(tags.size());
        assertThat(searchResultDTO.getSearchKeyword()).isEqualTo(keyword);
        for (TagSimpleDTO tag : searchResultDTO.getTags()) {
            Tag expected = tagMap.get(tag.getId());
            assertThat(tag.getName()).isEqualTo(expected.getName());
            assertThat(tag.getDescription()).isEqualTo(expected.getDescription());
        }
    }
}
