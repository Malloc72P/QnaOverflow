package scra.qnaboard.web.dto.question.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ParsedSearchQuestionDTO {

    public static final Long DEFAULT_AUTHOR_ID = -1L;
    public static final Long DEFAULT_ANSWERS = -1L;
    public static final Long DEFAULT_SCORE = Long.MIN_VALUE;
    public static final String DEFAULT_TITLE = "";

    private long authorId = DEFAULT_AUTHOR_ID;
    private long answers = DEFAULT_ANSWERS;
    private long score = DEFAULT_SCORE;
    private String title = DEFAULT_TITLE;
    private List<String> tags = new ArrayList<>();

    public void addTag(String tag) {
        tags.add(tag);
    }

    public boolean hasAuthorId() {
        return authorId != DEFAULT_AUTHOR_ID;
    }

    public boolean hasAnswers() {
        return answers != DEFAULT_ANSWERS;
    }

    public boolean hasTitle() {
        return !title.equals(DEFAULT_TITLE);
    }

    public boolean hasScore() {
        return score != DEFAULT_SCORE;
    }

    public boolean hasTags() {
        return tags.size() > 0;
    }

    public String searchInput() {
        StringBuilder sb = new StringBuilder();
        if (hasTitle()) {
            sb.append("\"").append(title).append("\" ");
        }
        if (hasScore()) {
            sb.append("score:").append(score).append(" ");
        }
        if (hasAnswers()) {
            sb.append("answers:").append(answers).append(" ");
        }
        if (hasAuthorId()) {
            sb.append("user:").append(authorId).append(" ");
        }
        if (hasTags()) {
            for (String tag : tags) {
                sb.append("[").append(tag).append("] ");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsedSearchQuestionDTO that = (ParsedSearchQuestionDTO) o;
        return getAuthorId() == that.getAuthorId() && getAnswers() == that.getAnswers() && getScore() == that.getScore() && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthorId(), getAnswers(), getScore(), getTitle(), getTags());
    }
}
