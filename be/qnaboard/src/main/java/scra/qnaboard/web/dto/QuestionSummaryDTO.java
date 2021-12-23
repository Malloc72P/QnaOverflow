package scra.qnaboard.web.dto;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.web.dto.exception.DtoConversionFailedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static scra.qnaboard.web.dto.exception.DtoConversionFailedException.ENTITY_OR_FIELD_IS_NULL;

@Getter
public class QuestionSummaryDTO {

    private long questionId;
    private String title;
    private long voteScore;
    private int answerCount;
    private long viewCount;
    @DateTimeFormat(pattern = "yyyy-mm-dd HH:MM")
    private LocalDateTime createDate;
    private String authorName;
    private List<TagDTO> tagDTOs = new ArrayList<>();

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
        return new QuestionSummaryDTO(
                question.getId(),
                question.getTitle(),
                question.getUpVoteCount() - question.getDownVoteCount(),
                question.getAnswers().size(),
                question.getViewCount(),
                question.getCreatedDate(),
                question.getAuthor().getNickname()
        );
    }

    private static void isConversionPossible(Question question) {
        if (question == null || question.getAuthor() == null) {
            throw new DtoConversionFailedException(ENTITY_OR_FIELD_IS_NULL);
        }
    }

    public void addTagDTO(TagDTO tagDTO) {
        tagDTOs.add(tagDTO);
    }
}
