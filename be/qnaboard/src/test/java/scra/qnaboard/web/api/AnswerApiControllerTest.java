package scra.qnaboard.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import scra.qnaboard.configuration.auth.SecurityConfig;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.AnswerService;
import scra.qnaboard.web.api.AnswerApiController;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.answer.create.CreateAnswerDTO;
import scra.qnaboard.web.dto.answer.edit.EditAnswerDTO;
import scra.qnaboard.web.dto.answer.edit.EditAnswerResultDTO;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static scra.qnaboard.web.utils.LocalDateTimeUtils.localeFormatter;

@WebMvcTest(
        controllers = AnswerApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
class AnswerApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnswerService answerService;

    @Test
    @WithMockUser
    void 답변글_생성_테스트() throws Exception {
        //given
        String authorName = "answer-author-name";
        LocalDateTime createdDate = LocalDateTime.of(2022, 1, 12, 12, 20);
        LocalDateTime lastModifiedDate = LocalDateTime.of(2022, 1, 12, 12, 20);
        //given
        AnswerDetailDTO answerDetailDTO = AnswerDetailDTO.builder()
                .authorName(authorName)
                .content("content-1")
                .createdDate(createdDate)
                .lastModifiedDate(lastModifiedDate)
                .voteScore(100)
                .build();
        //given
        long memberId = 1L;
        long questionId = 2L;
        String content = answerDetailDTO.getContent();
        given(answerService.createAnswer(memberId, questionId, content))
                .willReturn(answerDetailDTO);
        //given
        Constructor<CreateAnswerDTO> constructor = CreateAnswerDTO.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CreateAnswerDTO createAnswerDTO = constructor.newInstance();
        ReflectionTestUtils.setField(createAnswerDTO, "content", "content-1");

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/questions/" + questionId + "/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "ko")
                        .content(new ObjectMapper().writeValueAsString(createAnswerDTO))
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(memberId, authorName, ""))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(Matchers.containsString(answerDetailDTO.getContent())),
                        content().string(Matchers.containsString(answerDetailDTO.getAuthorName())),
                        content().string(Matchers.containsString(answerDetailDTO.getVoteScore() + "")),
                        content().string(Matchers.containsString(localeFormatter(Locale.KOREA).format(createdDate))),
                        content().string(Matchers.containsString(localeFormatter(Locale.KOREA).format(lastModifiedDate)))
                );
    }

    @Test
    @WithMockUser
    void 답변글_삭제_테스트() throws Exception {
        //given
        String authorName = "answer-author-name";
        //given
        AnswerDetailDTO answerDetailDTO = AnswerDetailDTO.builder()
                .authorName(authorName)
                .content("content-1")
                .voteScore(100)
                .build();
        //given
        long memberId = 1L;
        long questionId = 2L;
        long answerId = 3L;

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/questions/" + questionId + "/answers/" + answerId)
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(memberId, authorName, ""))
        );

        //then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 답변글_수정_테스트() throws Exception {
        //given
        String authorName = "answer-author-name";
        String content = "content-1";
        LocalDateTime lastModifiedDate = LocalDateTime.of(2022, 1, 12, 12, 20);
        //given
        EditAnswerResultDTO editAnswerResultDTO = EditAnswerResultDTO.builder()
                .content(content)
                .lastModifiedDate(lastModifiedDate)
                .build();
        //given
        long memberId = 1L;
        long questionId = 2L;
        long answerId = 3L;
        //given
        given(answerService.editAnswer(memberId, answerId, content))
                .willReturn(editAnswerResultDTO);
        //given
        Constructor<EditAnswerDTO> constructor = EditAnswerDTO.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        EditAnswerDTO editAnswerDTO = constructor.newInstance();
        ReflectionTestUtils.setField(editAnswerDTO, "content", content);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/questions/" + questionId + "/answers/" + answerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "ko")
                        .content(new ObjectMapper().writeValueAsString(editAnswerDTO))
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(memberId, authorName, ""))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.content", is(content)),
                        jsonPath("$.lastModifiedDate", is(localeFormatter(Locale.KOREA).format(lastModifiedDate)))
                );
    }
}
