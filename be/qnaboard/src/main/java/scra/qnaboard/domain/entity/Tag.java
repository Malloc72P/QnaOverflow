package scra.qnaboard.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String color;

    public Tag(Member author, String name, String color) {
        this.author = author;
        this.name = name;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getId(), tag.getId()) &&
                Objects.equals(getName(), tag.getName()) &&
                Objects.equals(getColor(), tag.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor());
    }
}
