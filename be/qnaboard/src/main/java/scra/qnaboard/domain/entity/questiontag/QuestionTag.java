package scra.qnaboard.domain.entity.questiontag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.BaseTimeEntity;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Question;

import javax.persistence.*;
import java.util.Objects;

/**
 * 질문글과 태그의 다대다 관계를 해소하기 위한, 중간 테이블에 해당하는 엔티티.
 * 질문글 아이디와 태그 아이디를 가지고 복합키를 만들고, 이를 기본키로 사용한다.
 * JPA를 가지고 복합키를 표현하기 위해 QuestionTagId를 사용한다
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionTag extends BaseTimeEntity {

    @EmbeddedId
    private QuestionTagId id;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @MapsId("questionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public QuestionTag(Tag tag, Question question) {
        this.tag = tag;
        this.question = question;
        id = new QuestionTagId(question.getId(), tag.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionTag that = (QuestionTag) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getTagId() {
        return id.tagId;
    }
}
