package scra.qnaboard.domain.entity.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class VoteId implements Serializable {

    private Long memberId;
    private Long postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteId voteId = (VoteId) o;
        return Objects.equals(getMemberId(), voteId.getMemberId()) && Objects.equals(getPostId(), voteId.getPostId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMemberId(), getPostId());
    }
}
