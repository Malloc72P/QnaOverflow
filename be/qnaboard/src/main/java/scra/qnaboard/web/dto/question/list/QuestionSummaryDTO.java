package scra.qnaboard.web.dto.question.list;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import scra.qnaboard.domain.entity.QuestionTag;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.web.dto.exception.DtoConversionFailedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static scra.qnaboard.web.dto.exception.DtoConversionFailedException.ENTITY_OR_FIELD_IS_NULL;

/**
 * 질문 목록조회를 위한 DTO. 질문목록의 요약정보라고 생각하면 된다. <br>
 * question-item-summary 프래그먼트로 해당 DTO를 넘겨서 뷰를 랜더링한다
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSummaryDTO {

    private long questionId;
    private String title;
    private long voteScore;
    private int answerCount;
    private long viewCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime createDate;
    private String authorName;
    private List<TagDTO> tags = new ArrayList<>();

    @QueryProjection
    public QuestionSummaryDTO(long questionId,
                              String title,
                              long voteScore,
                              int answerCount,
                              long viewCount,
                              LocalDateTime createDate,
                              String authorName) {
        this.questionId = questionId;
        this.title = title;
        this.voteScore = voteScore;
        this.answerCount = answerCount;
        this.viewCount = viewCount;
        this.createDate = createDate;
        this.authorName = authorName;
    }

    public static QuestionSummaryDTO from(Question question) {
        isConversionPossible(question);
        QuestionSummaryDTO dto = new QuestionSummaryDTO();

        dto.questionId = question.getId();
        dto.title = question.getTitle();
        dto.voteScore = question.getUpVoteCount() - question.getDownVoteCount();
        dto.answerCount = question.getAnswers().size();
        dto.viewCount = question.getViewCount();
        dto.createDate = question.getCreatedDate();
        dto.authorName = question.getAuthor().getNickname();

        question.getQuestionTags()
                .stream()
                .map(QuestionTag::getTag)
                .map(TagDTO::from)
                .forEach(dto::addTag);

        return dto;
    }

    private static void isConversionPossible(Question question) {
        if (question == null || question.getAuthor() == null || question.getAnswers() == null || question.getQuestionTags() == null) {
            throw new DtoConversionFailedException(ENTITY_OR_FIELD_IS_NULL);
        }
    }

    public void addTag(TagDTO tagDTO) {
        tags.add(tagDTO);
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
