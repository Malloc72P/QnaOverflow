package scra.qnaboard.domain.entity.vote;

import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;

import javax.persistence.*;
import java.util.Objects;

/**
 * 게시글의 추천 비추천 기능을 위한 투표 엔티티.
 *
 */
@Getter
@Entity
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    public Vote(Member member, Post post, VoteType voteType) {
        this.member = member;
        this.post = post;
        this.voteType = voteType;
    }

    /**
     * 같은 유형의 투표인지 여부를 반환함
     */
    public boolean isSameVote(VoteType voteType) {
        return this.voteType.equals(voteType);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(getId(), vote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
