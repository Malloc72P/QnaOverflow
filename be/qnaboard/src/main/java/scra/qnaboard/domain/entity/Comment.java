package scra.qnaboard.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.service.exception.comment.edit.CommentPropertyIsEmptyException;

import javax.persistence.*;
import java.util.Objects;

/**
 * 대댓글에 대한 엔티티.
 * 최상위 대댓글인 경우 parentComment가 null이다.
 * parentPost 절대로 null일 수 없고 반드시 존재해야 한다
 * 대댓글 엔티티는 부모 엔티티만 ManyToOne으로 가지고 있게 한다.
 * 대댓글을 디비에서 가져올때는 댓글의 계층구조는 고려하지 않고, 부모게시글 아이디(parentPost)로 한번에 다 가져온다.
 * 댓글의 계층관계는 parentComment값을 가지고 애플리케이션에서 조립한다.
 * 이렇게 하면 디비에 가해지는 부담이 좀 줄어들지 않을까 라고 생각해서 이렇게 구현했습니다.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    private boolean deleted = false;

    public Comment(Member author, String content, Post parentPost, Comment parentComment) {
        this.author = author;
        this.content = content;
        this.parentPost = parentPost;
        this.parentComment = parentComment;
    }

    /**
     * 댓글을 삭제함
     */
    public void delete() {
        deleted = true;
    }

    /**
     * 댓글의 소유자가 아닌지 여부를 반환함
     */
    public boolean isNotOwner(Member member) {
        return !member.equals(author);
    }

    /**
     * 댓글을 수정함
     */
    public void update(String content) {
        if (!StringUtils.hasText(content)) {
            throw new CommentPropertyIsEmptyException(content);
        }
        if (this.content.equals(content)) {
            return;
        }
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getId(), comment.getId()) &&
                Objects.equals(getContent(), comment.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
