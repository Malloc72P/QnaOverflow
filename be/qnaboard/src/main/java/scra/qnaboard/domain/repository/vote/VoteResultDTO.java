package scra.qnaboard.domain.repository.vote;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import scra.qnaboard.domain.entity.vote.VoteType;

import java.util.List;

@Getter
public class VoteResultDTO {

    private static final long POSITIVE = 1L;
    private static final long NEGATIVE = -1L;

    private long postId;
    private long count = 0L;
    private VoteType voteType;

    @QueryProjection
    public VoteResultDTO(long postId, long count, VoteType voteType) {
        this.postId = postId;
        this.count = count;
        this.voteType = voteType;
    }

    public long value() {
        long sign = voteType == VoteType.UP ? POSITIVE : NEGATIVE;
        return sign * count;
    }

    public static long countVoteScore(List<VoteResultDTO> voteResult) {
        //점수 계산하고 반환
        return voteResult.stream()
                .mapToLong(VoteResultDTO::value)
                .sum();
    }
}
