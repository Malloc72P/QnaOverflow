package scra.qnaboard.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.answer.AnswerRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.answer.create.CreateAnswerDTO;
import scra.qnaboard.web.dto.answer.edit.EditAnswerDTO;

import java.lang.reflect.Constructor;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static scra.qnaboard.web.utils.LocalDateTimeUtils.localeFormatter;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class AnswerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @WithMockUser
    void 답변글_생성_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String content = "answer-content";
        //given
        Constructor<CreateAnswerDTO> constructor = CreateAnswerDTO.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CreateAnswerDTO createAnswerDTO = constructor.newInstance();
        ReflectionTestUtils.setField(createAnswerDTO, "content", content);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/questions/" + question.getId() + "/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        .content(new ObjectMapper().writeValueAsString(createAnswerDTO))
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(author.getId(), author.getNickname(), author.getEmail())));

        //then
        AnswerDetailDTO answerDTO = (AnswerDetailDTO) resultActions.andReturn()
                .getModelAndView()
                .getModel()
                .get("answer");
        long answerId = answerDTO.getAnswerId();
        Answer findAnswer = answerRepository.findById(answerId).orElse(null);
        assertThat(findAnswer).isNotNull();
        //then
        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        content().string(Matchers.containsString(content)),
                        content().string(Matchers.containsString(author.getNickname())),
                        content().string(Matchers.containsString(localeFormatter(Locale.KOREA).format(findAnswer.getCreatedDate()))),
                        content().string(Matchers.containsString(localeFormatter(Locale.KOREA).format(findAnswer.getLastModifiedDate())))
                );
    }

    @Test
    @WithMockUser
    void 답변글_삭제_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        Answer answer = answerRepository.save(new Answer(author, "content-1", question));

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/questions/" + question.getId() + "/answers/" + answer.getId())
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(author)));

        //then
        resultActions.andExpect(status().isOk());
        Answer findAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(findAnswer).isNotNull();
        assertThat(findAnswer.isDeleted()).isTrue();
    }

    @Test
    @WithMockUser
    void 답변글_수정_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        Answer answer = answerRepository.save(new Answer(author, "content-1", question));
        //given
        String newContent = "new-answer-content";
        //given
        Constructor<EditAnswerDTO> constructor = EditAnswerDTO.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        EditAnswerDTO editAnswerDTO = constructor.newInstance();
        ReflectionTestUtils.setField(editAnswerDTO, "content", newContent);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/questions/" + question.getId() + "/answers/" + answer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        .content(new ObjectMapper().writeValueAsString(editAnswerDTO))
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(author)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.content", is(newContent)),
                        jsonPath("$.lastModifiedDate", is(localeFormatter(Locale.KOREA).format(question.getLastModifiedDate())))
                );
    }
}
