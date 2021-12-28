package scra.qnaboard.web.dto.question.detail;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import scra.qnaboard.domain.entity.QuestionTag;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.web.dto.tag.TagDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuestionDetailDTO {

    private long questionId;
    private String title;
    private String content;
    private long voteScore;
    private List<AnswerDTO> answers = new ArrayList<>();
    private long viewCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime lastModifiedDate;
    private long authorId;
    private String authorName;
    private List<TagDTO> tags = new ArrayList<>();

    public static QuestionDetailDTO from(Question question) {
        QuestionDetailDTO detailDTO = new QuestionDetailDTO();
        detailDTO.questionId = question.getId();
        detailDTO.title = question.getTitle();
        detailDTO.content = question.getContent();
        detailDTO.voteScore = question.getUpVoteCount() - question.getDownVoteCount();
        detailDTO.viewCount = question.getViewCount();
        detailDTO.createdDate = question.getCreatedDate();
        detailDTO.lastModifiedDate = question.getLastModifiedDate();
        detailDTO.authorId = question.getAuthor().getId();
        detailDTO.authorName = question.getAuthor().getNickname();

        detailDTO.tags = question.getQuestionTags().stream()
                .map(QuestionTag::getTag)
                .map(tag -> new TagDTO(tag.getId(), question.getId(), tag.getName()))
                .collect(Collectors.toList());


        detailDTO.answers = question.getAnswers().stream()
                .map(AnswerDTO::from)
                .collect(Collectors.toList());

        return detailDTO;
    }
}
