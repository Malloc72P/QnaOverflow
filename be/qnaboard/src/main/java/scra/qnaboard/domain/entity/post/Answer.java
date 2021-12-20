package scra.qnaboard.domain.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.Member;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

/**
 * 답변글에 대한 엔티티 <br>
 * 답변글 엔티티를 만들려면 반드시 질문글이 있어야 만들 수 있도록 생성자로 강제함
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends Post {

    private boolean accepted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", referencedColumnName = "post_id")
    private Question question;

    public Answer(Member author, String content, Question question) {
        super(author, content);
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Answer answer = (Answer) o;
        return isAccepted() == answer.isAccepted();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isAccepted());
    }
}
