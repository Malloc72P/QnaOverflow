package scra.qnaboard.domain.entity.vote;

import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Vote {

    @EmbeddedId
    private VoteId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
    private Member member;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    public Vote(Member member, Post post, VoteType voteType) {
        this.member = member;
        this.post = post;
        this.id = new VoteId(member.getId(), post.getId());
        this.voteType = voteType;
    }
}
