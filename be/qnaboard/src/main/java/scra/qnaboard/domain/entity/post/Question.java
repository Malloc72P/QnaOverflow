package scra.qnaboard.domain.entity.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.service.exception.question.edit.QuestionPropertyIsEmptyException;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 질문글에 대한 엔티티.
 * 추가로 조회수에 해당하는 viewCount, 제목인 title, 그리고 태그목록인 questionTags를 가지고 있다.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Post {

    @OneToMany(mappedBy = "question")
    private final List<QuestionTag> questionTags = new ArrayList<>();
    private long viewCount = 0;
    private String title;

    @Builder
    public Question(Member author, String content, String title) {
        super(author, content);
        this.title = title;
    }

    /**
     * 질문글을 수정함
     *
     * @param title   새로운 제목
     * @param content 새로운 내용
     */
    public void update(String title, String content) {
        if (!StringUtils.hasText(title) || !StringUtils.hasText(content)) {
            throw new QuestionPropertyIsEmptyException(title, content);
        }
        this.title = title;
        this.content = content;
    }

    /**
     * 새로운 태그를 추가함.
     * 다만 cascade 설정이 되어있지 않으므로 이 메서드만 호출해서는 QuestionTag가 저장되지 않는다.
     * 따로 쿼리를 실행해서 추가해야 디비에 반영된다
     */
    public void addQuestionTag(QuestionTag questionTag) {
        questionTags.add(questionTag);
    }

    /**
     * 태그 정보를 초기화함
     * 다만 cascade 설정이 되어있지 않으므로 이 메서드만 호출해서는 QuestionTag가 초기화 되지 않는다.
     * 따로 쿼리를 실행해서 추가해야 디비에 반영된다
     */
    public void resetTags() {
        questionTags.clear();
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
