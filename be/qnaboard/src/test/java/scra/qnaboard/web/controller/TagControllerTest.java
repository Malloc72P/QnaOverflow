package scra.qnaboard.web.controller;

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
import scra.qnaboard.service.TagService;
import scra.qnaboard.web.dto.tag.list.TagDTO;
import scra.qnaboard.web.dto.tag.list.TagListDTO;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TagController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageSource message;

    @MockBean
    private TagService tagService;

    @Test
    @WithMockUser
    void 태그목록조회_테스트() throws Exception {
        //given
        List<TagDTO> tags = new ArrayList<>();
        tags.add(TagDTO.builder().tagName("tag-1").build());
        tags.add(TagDTO.builder().tagName("tag-2").build());
        //given
        given(tagService.tagList()).willReturn(new TagListDTO(tags));
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/tags")
                        .with(csrf())
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpectAll(
                        content().string(Matchers.containsString(tags.get(0).getTagName())),
                        content().string(Matchers.containsString(tags.get(1).getTagName()))
                );
    }

    @Test
    @WithMockUser
    void 태그생성폼_테스트() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/tags/form")
                        .with(csrf())
        );
        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 태그수정폼_테스트() throws Exception {
        //given
        long tagId = 1L;
        TagDTO tagDTO = TagDTO.builder()
                .tagId(tagId)
                .tagName("tag-name")
                .build();
        //given
        given(tagService.tagById(tagId)).willReturn(tagDTO);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/tags/" + tagId + "/edit-form")
                        .with(csrf())
        );
        //then
        resultActions.andExpect(status().isOk())
                .andExpectAll(
                        content().string(Matchers.containsString(tagDTO.getTagId() + "")),
                        content().string(Matchers.containsString(tagDTO.getTagName()))
                );
    }

    @Test
    @WithMockUser
    void 태그생성_테스트() throws Exception {
        //given
        Locale locale = Locale.KOREA;
        String titleMessage = this.message.getMessage("ui.notify.tag.create.title", null, locale);
        String contentMessage = this.message.getMessage("ui.notify.tag.create.content", null, locale);
        //given
        titleMessage = URLEncoder.encode(titleMessage, StandardCharsets.UTF_8.toString());
        contentMessage = URLEncoder.encode(contentMessage, StandardCharsets.UTF_8.toString());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/tags")
                        .header("Accept-Language", "ko")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "tag-name")
                        .param("description", "tag-description")
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(1L, "", ""))
        );

        //then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notify?title=" + titleMessage + "&content=" + contentMessage));
    }

    @Test
    @WithMockUser
    void 태그수정_테스트() throws Exception {
        //given
        long tagId = 1L;
        Locale locale = Locale.KOREA;
        String titleMessage = this.message.getMessage("ui.notify.tag.edit.title", null, locale);
        String contentMessage = this.message.getMessage("ui.notify.tag.edit.content", null, locale);
        //given
        titleMessage = URLEncoder.encode(titleMessage, StandardCharsets.UTF_8.toString());
        contentMessage = URLEncoder.encode(contentMessage, StandardCharsets.UTF_8.toString());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/tags/" + tagId + "/edit")
                        .header("Accept-Language", "ko")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "new-tag-name")
                        .param("description", "new-tag-description")
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(1L, "", ""))
        );

        //then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notify?title=" + titleMessage + "&content=" + contentMessage));
    }

    @Test
    @WithMockUser
    void 태그삭제_테스트() throws Exception {
        //given
        long tagId = 1L;
        Locale locale = Locale.KOREA;
        String titleMessage = this.message.getMessage("ui.notify.tag.delete.title", null, locale);
        String contentMessage = this.message.getMessage("ui.notify.tag.delete.content", null, locale);
        //given
        titleMessage = URLEncoder.encode(titleMessage, StandardCharsets.UTF_8.toString());
        contentMessage = URLEncoder.encode(contentMessage, StandardCharsets.UTF_8.toString());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/tags/" + tagId + "/delete")
                        .header("Accept-Language", "ko")
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(1L, "", ""))
        );

        //then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notify?title=" + titleMessage + "&content=" + contentMessage));
    }
}
