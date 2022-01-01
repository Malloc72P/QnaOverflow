package scra.qnaboard.domain.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.service.exception.QuestionPropertyIsEmptyException;

import javax.persistence.Entity;
import java.util.Objects;

/**
 * 질문글에 대한 엔티티 <br>
 * 답변글을 List로 가지고 있다 <br>
 * <p>
 * QuestionTag를 CascadeType.ALL로 가진다. 그래서 Question을 영속화할때 가지고 있는 모든 QuestionTag도 함께 영속화한다. <br>
 * 반대로 Question을 지우면 연관된 모든 QuestionTag가 지워진다.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Post {

    private long viewCount = 0;

    private String title;

    public Question(Member author, String content, String title) {
        super(author, content);
        this.title = title;
    }

    public boolean isNotOwner(Member member) {
        return !member.equals(author);
    }

    public void update(String title, String content) {
        if (!StringUtils.hasText(title) || !StringUtils.hasText(content)) {
            throw new QuestionPropertyIsEmptyException(title, content);
        }
        this.title = title;
        this.content = content;
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
