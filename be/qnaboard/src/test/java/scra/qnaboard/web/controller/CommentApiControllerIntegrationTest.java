package scra.qnaboard.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.comment.CommentRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.web.dto.comment.CommentDTO;
import scra.qnaboard.web.dto.comment.create.CreateCommentDTO;
import scra.qnaboard.web.dto.comment.edit.EditCommentDTO;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CommentApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MessageSource message;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @WithMockUser
    void 댓글_생성_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        Long parentCommentId = null;
        String content = "comment-content";
        //given
        CreateCommentDTO createCommentDTO = CreateCommentDTO.builder()
                .content(content)
                .parentCommentId(parentCommentId)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/posts/" + question.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        .content(new ObjectMapper().writeValueAsString(createCommentDTO))
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(author)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        content().string(Matchers.containsString(content)),
                        content().string(Matchers.containsString(author.getNickname()))
                );
        //then
        CommentDTO commentDTO = (CommentDTO) resultActions.andReturn()
                .getModelAndView()
                .getModel()
                .get("comment");
        Comment comment = commentRepository.findById(commentDTO.getCommentId()).orElse(null);
        assertThat(comment).isNotNull();
        assertThat(comment.getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser
    void 댓글_삭제_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        Comment comment = commentRepository.save(new Comment(author, "comment-content", question, null));
        //given
        Locale locale = Locale.KOREA;
        String deleteAuthorName = this.message.getMessage("ui.comment.delete.author-name", null, locale);
        String deleteContent = this.message.getMessage("ui.comment.delete.content", null, locale);

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/posts/" + question.getId() + "/comments/" + comment.getId())
                        .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(author)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.deletedAuthorName", is(deleteAuthorName)),
                        jsonPath("$.deletedContentName", is(deleteContent))
                );
        //then
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(findComment).isNotNull();
        assertThat(findComment.isDeleted()).isTrue();
    }

    @Test
    @WithMockUser
    void 댓글_수정_테스트() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        Comment comment = commentRepository.save(new Comment(author, "comment-content", question, null));
        //given
        EditCommentDTO editCommentDTO = new EditCommentDTO("new-comment-content");

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/posts/" + question.getId() + "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                        .content(new ObjectMapper().writeValueAsString(editCommentDTO))
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(author)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(editCommentDTO.getContent())));
        //then
        Comment findComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo(editCommentDTO.getContent());
    }
}
