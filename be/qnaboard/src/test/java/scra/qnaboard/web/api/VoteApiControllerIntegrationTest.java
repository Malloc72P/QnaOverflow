package scra.qnaboard.web.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.domain.repository.vote.VoteSimpleQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class VoteApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private VoteSimpleQueryRepository voteSimpleQueryRepository;

    @Test
    @WithMockUser
    @DisplayName("투표_테스트")
    void testVote() throws Exception {
        //given
        Member member = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(member, "content-1", "title-1"));

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/api/posts/" + question.getId() + "/votes/?voteType=" + VoteType.UP)
                        .with(csrf())
                        .sessionAttr("user", new SessionUser(member)));

        //then
        resultActions.andExpect(status().isOk());
        //then
        Vote vote = voteSimpleQueryRepository.findById(member, question).orElse(null);
        assertThat(vote).isNotNull();
        assertThat(vote.getVoteType()).isEqualTo(VoteType.UP);
    }
}
