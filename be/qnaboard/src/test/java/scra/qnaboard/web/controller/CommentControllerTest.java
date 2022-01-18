package scra.qnaboard.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import scra.qnaboard.configuration.auth.SecurityConfig;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.service.CommentService;
import scra.qnaboard.web.dto.comment.CommentDTO;
import scra.qnaboard.web.dto.comment.create.CreateCommentDTO;
import scra.qnaboard.web.dto.comment.delete.CommentDeleteResultDTO;
import scra.qnaboard.web.dto.comment.edit.EditCommentDTO;
import scra.qnaboard.web.dto.comment.edit.EditCommentResultDTO;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static scra.qnaboard.web.utils.LocalDateTimeUtils.localeFormatter;

@WebMvcTest(
        controllers = CommentController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MessageSource messageSource;

    @Test
    @WithMockUser
    void 댓글_생성_테스트() throws Exception {
        //given
        long commentId = 1L;
        long authorId = 2L;
        long postId = 2L;
        Long parentCommentId = null;
        String content = "comment-content";
        String authorName = "author-name";
        LocalDateTime createdDate = LocalDateTime.of(2022, 1, 12, 12, 20);
        //given
        CreateCommentDTO createCommentDTO = CreateCommentDTO.builder()
                .content(content)
                .parentCommentId(parentCommentId)
                .build();
        //given
        CommentDTO commentDTO = CommentDTO.builder()
                .commentId(commentId)
                .authorName(authorName)
                .content(content)
                .createdDate(createdDate)
                .build();
        //given
        given(commentService.createComment(authorId, postId, parentCommentId, content))
                .willReturn(commentDTO);

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/posts/" + postId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        .content(new ObjectMapper().writeValueAsString(createCommentDTO))
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(authorId, authorName, ""))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(Matchers.containsString(commentDTO.getContent())),
                        content().string(Matchers.containsString(commentDTO.getAuthorName())),
                        content().string(Matchers.containsString(localeFormatter(Locale.KOREA).format(createdDate)))
                );

    }

    @Test
    @WithMockUser
    void 댓글_삭제_테스트() throws Exception {
        //given
        long commentId = 1L;
        long authorId = 2L;
        long postId = 2L;
        Locale locale = Locale.KOREA;
        //given
        CommentDeleteResultDTO deleteResultDTO = CommentDeleteResultDTO.builder()
                .deletedAuthorName(messageSource.getMessage("ui.comment.delete.author-name", null, locale))
                .deletedContentName(messageSource.getMessage("ui.comment.delete.content", null, locale))
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/posts/" + postId + "/comments/" + commentId)
                        .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(authorId, "authorName", ""))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.deletedAuthorName", is(deleteResultDTO.getDeletedAuthorName())),
                        jsonPath("$.deletedContentName", is(deleteResultDTO.getDeletedContentName()))
                );
    }

    @Test
    @WithMockUser
    void 댓글_수정_테스트() throws Exception {
        //given
        long commentId = 1L;
        long authorId = 2L;
        long postId = 2L;
        String authorName = "author-name";
        String newCommentContent = "new-comment-content";
        //given
        EditCommentDTO editCommentDTO = new EditCommentDTO(newCommentContent);
        //given
        EditCommentResultDTO editCommentResultDTO = new EditCommentResultDTO(newCommentContent);
        //given
        given(commentService.editComment(authorId, commentId, newCommentContent))
                .willReturn(editCommentResultDTO);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/posts/" + postId + "/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        .content(new ObjectMapper().writeValueAsString(editCommentDTO))
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(authorId, authorName, ""))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.content", is(newCommentContent))
                );
    }
}
