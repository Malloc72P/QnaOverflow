package scra.qnaboard.domain.entity.questiontag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTagId implements Serializable {
    protected Long questionId;
    protected Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionTagId that = (QuestionTagId) o;
        return Objects.equals(getQuestionId(), that.getQuestionId()) && Objects.equals(getTagId(), that.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestionId(), getTagId());
    }
}
