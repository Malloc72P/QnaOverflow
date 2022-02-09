package scra.qnaboard.dto.question.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SearchQuestionDTO을 편하게 사용할 수 있도록 변환한 DTO.
 * 질문목록 검색 쿼리를 만들 때 해당 DTO를 사용한다.
 * hasXXX() 메서드를 사용해서 검색 파라미터가 있는지를 확인하고, 있으면 Where조건을 추가하는 식으로 작동한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ParsedSearchQuestionDTO {
    //검색 파라미터의 기본값.
    public static final Long DEFAULT_AUTHOR_ID = -1L;
    public static final Long DEFAULT_ANSWERS = -1L;
    public static final Long DEFAULT_SCORE = Long.MIN_VALUE;
    public static final String DEFAULT_TITLE = "";
    //검색 파라미터.
    private long authorId = DEFAULT_AUTHOR_ID;
    private long answers = DEFAULT_ANSWERS;
    private long score = DEFAULT_SCORE;
    private String title = DEFAULT_TITLE;
    private List<String> tags = new ArrayList<>();

    /**
     * 검색에 사용할 태그를 추가함
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * 검색 파라미터에 사용자 아이디 검색이 있는지 여부를 반환함
     */
    public boolean hasAuthorId() {
        return authorId != DEFAULT_AUTHOR_ID;
    }

    /**
     * 검색 파라미터에 답변글 개수 검색이 있는지 여부를 반환함
     */
    public boolean hasAnswers() {
        return answers != DEFAULT_ANSWERS;
    }

    /**
     * 검색 파라미터에 제목 검색이 있는지 여부를 반환함
     */
    public boolean hasTitle() {
        return !title.equals(DEFAULT_TITLE);
    }

    /**
     * 검색 파라미터에 추천점수 검색이 있는지 여부를 반환함
     */
    public boolean hasScore() {
        return score != DEFAULT_SCORE;
    }

    /**
     * 검색 파라미터에 태그 검색이 있는지 여부를 반환함
     */
    public boolean hasTags() {
        return tags.size() > 0;
    }

    /**
     * ParsedSearchQuestionDTO를 검색창에 들어가는 한줄짜리 문자열로 변환하고 반환함.
     */
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
