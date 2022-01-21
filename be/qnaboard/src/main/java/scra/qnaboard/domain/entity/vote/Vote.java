package scra.qnaboard.domain.entity.vote;

import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    public Vote(Member member, Post post, VoteType voteType) {
        this.member = member;
        this.post = post;
//        this.id = new VoteId(member.getId(), post.getId());
        this.voteType = voteType;
    }

    public boolean isSameVote(VoteType voteType) {
        return this.voteType.equals(voteType);
    }

//    public void changeVoteType(VoteType newVote) {
//        if (isSameVote(newVote)) {
//            //동일한 표로 변경을 시도하면 예외를 발생시킨다
//            throw new DuplicateVoteException();
//        }
//
//        //동일한 표가 아니라면 수정한다
//        this.voteType = newVote;
//    }

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
