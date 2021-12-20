package scra.qnaboard.domain.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.BaseTimeEntity;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 게시글에 대한 엔티티 <br>
 * 게시글은 추상클래스이고, 실제로 사용할 엔티티는 Answer와 Question 엔티티임 <br>
 * 상속관계를 테이블에서 표현할 때는 싱글테이블 전략을 사용하기로 함
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type")
public abstract class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Member author;

    private String content;

    private Long upVoteCount = 0L;
    private Long downVoteCount = 0L;

    @OneToMany(mappedBy = "parentPost", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Post(Member author, String content) {
        this.author = author;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId()) &&
                Objects.equals(getContent(), post.getContent()) &&
                Objects.equals(getUpVoteCount(), post.getUpVoteCount()) &&
                Objects.equals(getDownVoteCount(), post.getDownVoteCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), getUpVoteCount(), getDownVoteCount());
    }
}
