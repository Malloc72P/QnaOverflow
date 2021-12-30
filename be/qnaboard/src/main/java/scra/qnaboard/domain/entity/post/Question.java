package scra.qnaboard.domain.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.QuestionTag;
import scra.qnaboard.domain.entity.Tag;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<QuestionTag> questionTags = new ArrayList<>();

    public Question(Member author, String content, String title) {
        super(author, content);
        this.title = title;
    }

    /**
     * 태그를 질문엔티티에 추가하는 메서드 <br>
     * 태그와 질문 엔티티 사이를 이어주는 QuestionTag는 CascadeType.ALL 설정이 걸려있어서, <br>
     * 따로 영속화하지 않아도 Question엔티티를 영속화할때 함께 처리된다
     *
     * @param tag 질문 엔티티에 추가할 태그 엔티티. QuestionTag아님!
     */
    public void addTag(Tag tag) {
        QuestionTag questionTag = new QuestionTag(tag, this);
        questionTags.add(questionTag);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
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
