package scra.qnaboard.domain.repository.vote;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scra.qnaboard.domain.entity.vote.VoteType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VoteResultDTOTest {

    @Test
    @DisplayName("점수를 집계할 수 있어야 함")
    void testVoteScore() {
        List<VoteResultDTO> voteResultDTOS = new ArrayList<>();

        voteResultDTOS.add(new VoteResultDTO(0L, 10L, VoteType.UP));
        voteResultDTOS.add(new VoteResultDTO(0L, 10L, VoteType.DOWN));

        test(voteResultDTOS, 0);
    }

    @Test
    @DisplayName("upvote만 있어도 집계할 수 있어야 함")
    void testVoteScoreWhenOnlyUpVote() {
        List<VoteResultDTO> voteResultDTOS = new ArrayList<>();

        voteResultDTOS.add(new VoteResultDTO(0L, 10L, VoteType.UP));

        test(voteResultDTOS, 10);
    }

    @Test
    @DisplayName("downvote만 있어도 집계할 수 있어야 함")
    void testVoteScoreWhenOnlyDownVote() {
        List<VoteResultDTO> voteResultDTOS = new ArrayList<>();

        voteResultDTOS.add(new VoteResultDTO(0L, 10L, VoteType.DOWN));

        test(voteResultDTOS, -10);
    }

    @Test
    @DisplayName("투표결과가 없어도 점수를 반환해야 함")
    void testVoteScoreWhenNoVoteData() {
        List<VoteResultDTO> voteResultDTOS = new ArrayList<>();

        test(voteResultDTOS, 0);
    }

    void test(List<VoteResultDTO> voteResultDTOS, long expected) {
        long voteScore = VoteResultDTO.countVoteScore(voteResultDTOS);

        assertThat(voteScore).isEqualTo(expected);
    }
}
