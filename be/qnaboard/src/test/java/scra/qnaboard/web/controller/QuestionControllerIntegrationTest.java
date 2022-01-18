package scra.qnaboard.web.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.web.dto.question.search.ParsedSearchQuestionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class QuestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    @WithMockUser
    void 질문_목록조회_테스트() throws Exception {
        //given
        Member member = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        List<Question> questions = new ArrayList<>();
        questions.add(questionRepository.save(new Question(member, "content-1", "title-1")));
        questions.add(questionRepository.save(new Question(member, "content-2", "title-2")));
        questions.add(questionRepository.save(new Question(member, "content-3", "title-3")));
        questions.add(questionRepository.save(new Question(member, "aaaaaaaaaaa", "asdfaaa")));
        questions.add(questionRepository.save(new Question(member, "aaaaaaaaaa", "asdfaaa")));
        //given
        ParsedSearchQuestionDTO searchQuestionDTO = new ParsedSearchQuestionDTO();
        searchQuestionDTO.setTitle("title-");

        //when
        ResultActions resultActions = mockMvc.perform(get("/questions"));

        //then
        resultActions.andExpect(status().isOk());
        for (Question question : questions) {
            resultActions.andExpectAll(
                    content().string(Matchers.containsString(question.getTitle())),
                    content().string(Matchers.containsString(question.getAuthor().getNickname()))
            );
        }
    }

    @Test
    void 질문_생성_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        String title = "title-1";
        String content = "content-1";
        //given
        List<Tag> tags = new ArrayList<>();
        tags.add(tagRepository.save(new Tag(author, "tag", "tag-description")));
        tags.add(tagRepository.save(new Tag(author, "tag", "tag-description")));
        tags.add(tagRepository.save(new Tag(author, "tag", "tag-description")));
        //given
        String tagIds = tags.stream()
                .map(Tag::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/questions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", title)
                        .param("content", content)
                        .param("tags", tagIds)
                        .sessionAttr("user", new SessionUser(author)));

        //then
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/questions/{.+}"));
        MvcResult mvcResult = resultActions.andReturn();
        //then
        Long questionId = Long.parseLong((String) mvcResult.getModelAndView().getModel().get("questionId"));
        Question findQuestion = questionRepository.findById(questionId).orElse(null);
        assertThat(findQuestion).isNotNull();
        assertThat(findQuestion.getTitle()).isEqualTo(title);
        assertThat(findQuestion.getContent()).isEqualTo(content);
        assertThat(findQuestion.getAuthor().getId()).isEqualTo(author.getId());
        assertThat(findQuestion.getQuestionTags().size()).isEqualTo(tags.size());
    }
}
