package scra.qnaboard.domain.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.service.exception.answer.edit.AnswerPropertyIsEmptyException;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 답변글에 대한 엔티티 <br>
 * 답변글 엔티티를 만들려면 반드시 질문글이 있어야 만들 수 있도록 생성자로 강제함
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends Post {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public Answer(Member author, String content, Question question) {
        super(author, content);
        this.question = question;
    }

    public void update(String content) {
        if (!StringUtils.hasText(content)) {
            throw new AnswerPropertyIsEmptyException(content);
        }
        this.content = content;
    }
}
