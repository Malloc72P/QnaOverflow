package scra.qnaboard.dto.answer;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.dto.comment.CommentDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 답변 게시글을 위한 DTO
 * 날짜 포맷은 Thymeleaf에서 직접 적용함(프래그먼트를 사용하다보니 애너테이션의 포매터가 적용이 안된다!)
 * 프래그먼트에 localDateTime만 넘기고 DTO는 안넘겨서 애너테이션 정보가 전달되지 않는 모양이다
 * 바꾸고 싶으면 post-controller.html 프래그먼트에서 수정하자
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerDetailDTO {

    private long answerId;
    private String content;
    private long voteScore;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private long authorId;
    private String authorName;
    private List<CommentDTO> comments = new ArrayList<>();

    @Builder
    @QueryProjection
    public AnswerDetailDTO(long answerId,
                           long voteScore,
                           String content,
                           LocalDateTime createdDate,
                           LocalDateTime lastModifiedDate,
                           long authorId,
                           String authorName) {
        this.answerId = answerId;
        this.voteScore = voteScore;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    /**
     * 엔티티를 DTO로 변환함
     * answer.getAuthor()로 인해 Lazy Loading이 발생함에 주의할 것.
     */
    public static AnswerDetailDTO from(Answer answer) {
        AnswerDetailDTO answerDTO = new AnswerDetailDTO();
        answerDTO.answerId = answer.getId();
        answerDTO.content = answer.getContent();
        answerDTO.createdDate = answer.getCreatedDate();
        answerDTO.lastModifiedDate = answer.getLastModifiedDate();
        answerDTO.authorId = answer.getAuthor().getId();
        answerDTO.authorName = answer.getAuthor().getNickname();

        return answerDTO;
    }

    /**
     * 답변글의 아이디로, 파라미터로 받은 newCommentMap에서, 댓글 List를 꺼내고, 이걸로 DTO의 댓글을 업데이트함
     */
    public void update(Map<Long, List<CommentDTO>> newCommentMap) {
        List<CommentDTO> comments = newCommentMap.get(answerId);
        updateComments(comments);
    }

    /**
     * DTO의 댓글을 업데이트함
     */
    private void updateComments(List<CommentDTO> comments) {
        if (comments == null) {
            return;
        }
        this.comments = comments;
    }
}
