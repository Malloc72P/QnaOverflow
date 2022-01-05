package scra.qnaboard.domain.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.service.exception.question.edit.QuestionPropertyIsEmptyException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 질문글에 대한 엔티티 <br>
 * 답변글을 List로 가지고 있다 <br>
 * <p>
 * QuestionTag와 Answer가 Question 엔티티와 다대일 관계를 가지고 있으나, Question에서는 매핑하지 않는다 <br>
 * QuestionTag나 Answer가 필요하다면 JPQL 쿼리를 날려서 가져오도록 하자
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Post {

    private long viewCount = 0;

    private String title;

    @OneToMany(mappedBy = "question")
    private final List<QuestionTag> questionTags = new ArrayList<>();

    public Question(Member author, String content, String title) {
        super(author, content);
        this.title = title;
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
