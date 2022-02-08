package scra.qnaboard.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.service.exception.tag.edit.TagPropertyIsEmptyException;

import javax.persistence.*;
import java.util.Objects;

/**
 * 질문글을 검색할 때 사용하는 태그에 대한 엔티티.
 * 태그와 질문글은 다대다 관계를 맺고 있다. 중간 테이블에 해당하는 QuestionTag 엔티티를 가지고 다대다 관계를 해소하고 있다.
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

    @Builder
    public Tag(Member author, String name, String description) {
        this.author = author;
        this.name = name;
        this.description = description;
    }

    /**
     * 태그의 소유자인지 여부를 반환함
     */
    public boolean isNotOwner(Member member) {
        return !member.equals(author);
    }

    /**
     * 태그를 수정함
     */
    public void update(String name, String description) {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(description)) {
            throw new TagPropertyIsEmptyException(name, description);
        }
        this.name = name;
        this.description = description;
    }

    /**
     * 태그를 삭제함
     */
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
