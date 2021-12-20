package scra.qnaboard.domain.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.Member;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 질문글에 대한 엔티티 <br>
 * 답변글을 List로 가지고 있는다
 *
 * @TODO 대댓글도 추가해야함!
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Post {

    private long viewCount = 0;

    private String title;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    public Question(Member author, String content, String title) {
        super(author, content);
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Question question = (Question) o;
        return getViewCount() == question.getViewCount() && Objects.equals(getTitle(), question.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getViewCount(), getTitle());
    }
}
