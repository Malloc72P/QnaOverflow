package scra.qnaboard.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.service.exception.tag.edit.TagPropertyIsEmptyException;

import javax.persistence.*;
import java.util.Objects;

/**
 * 회원에 대한 Member 엔티티.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    private String name;

    private String description;

    private boolean deleted = false;

    public Tag(Member author, String name, String description) {
        this.author = author;
        this.name = name;
        this.description = description;
    }

    public boolean isNotOwner(Member member) {
        return !member.equals(author);
    }

    public void update(String name, String description) {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(description)) {
            throw new TagPropertyIsEmptyException(name, description);
        }
        this.name = name;
        this.description = description;
    }

    public void delete() {
        deleted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getId(), tag.getId()) &&
                Objects.equals(getName(), tag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

}
