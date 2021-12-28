package scra.qnaboard.web.dto.question.detail;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import scra.qnaboard.domain.entity.post.Answer;

import java.time.LocalDateTime;

@Getter
public class AnswerDTO {

    private long answerId;
    private String content;
    private long voteScore;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime lastModifiedDate;
    private long authorId;
    private String authorName;

    public static AnswerDTO from(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.answerId = answer.getId();
        answerDTO.content = answer.getContent();
        answerDTO.voteScore = answer.getUpVoteCount() - answer.getDownVoteCount();
        answerDTO.createdDate = answer.getCreatedDate();
        answerDTO.lastModifiedDate = answer.getLastModifiedDate();
        answerDTO.authorId = answer.getAuthor().getId();
        answerDTO.authorName = answer.getAuthor().getNickname();

        return answerDTO;
    }
}
