package scra.qnaboard.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @WithMockUser
    void 회원목록조회테스트() throws Exception {
        //given
        List<Member> members = new ArrayList<>();
        members.add(memberRepository.save(new Member("nickname1", "email", MemberRole.USER)));
        members.add(memberRepository.save(new Member("nickname2", "email", MemberRole.USER)));
        members.add(memberRepository.save(new Member("nickname3", "email", MemberRole.ADMIN)));
        members.add(memberRepository.save(new Member("nickname4", "email", MemberRole.USER)));
        int deletedMemberIndex = members.size() - 1;
        ReflectionTestUtils.setField(members.get(deletedMemberIndex), "deleted", true);
        memberRepository.deleteMemberById(members.get(deletedMemberIndex).getId());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/members")
                        .with(csrf()));

        //then
        resultActions.andExpect(status().isOk());
        for (Member member : members) {
            if (!member.isDeleted()) {
                resultActions.andExpect(content().string(containsString(member.getNickname())));
            }
            if (member.isDeleted()) {
                resultActions.andExpect(content().string(not(containsString(member.getNickname()))));
            }
        }
    }
}
