package scra.qnaboard.web.dto.question.detail;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.QuestionTag;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.tag.TagDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 질문 상세보기 DTO
 * 날짜 포맷은 Thymeleaf에서 직접 적용함(프래그먼트를 사용하다보니 애너테이션의 포매터가 적용이 안된다!)
 * 프래그먼트에 localDateTime만 넘기고 DTO는 안넘겨서 애너테이션 정보가 전달되지 않는 모양이다
 * 바꾸고 싶으면 post-controller.html 프래그먼트에서 수정하자
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionDetailDTO {

    private long questionId;
    private String title;
    private String content;
    private long voteScore;
    private List<AnswerDetailDTO> answers = new ArrayList<>();
    private long viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private long authorId;
    private String authorName;
    private List<TagDTO> tags = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();

    @QueryProjection
    public QuestionDetailDTO(long questionId,
                             String title,
                             String content,
                             long voteScore,
                             long viewCount,
                             LocalDateTime createdDate,
                             LocalDateTime lastModifiedDate,
                             long authorId,
                             String authorName) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.voteScore = voteScore;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    /**
     * 엔티티에서 DTO로 변환하는 메서드. 태그랑 답변게시글은 따로 넣을 것
     *
     * @param question 대상 엔티티
     * @return QuestionDetailDTO
     */
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

        return detailDTO;
    }

    public void updateAnswer(List<AnswerDetailDTO> answers) {
        if (answers == null) {
            return;
        }
        this.answers = answers;
    }

    public void updateTags(List<TagDTO> tags) {
        if (tags == null) {
            return;
        }
        this.tags = tags;
    }

    public void updateComments(List<CommentDTO> comments) {
        if (comments == null) {
            return;
        }
        this.comments = comments;
    }
}
