package scra.qnaboard.dto.comment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import scra.qnaboard.domain.entity.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static scra.qnaboard.utils.LocalDateTimeUtils.STRING_DATE_TIME_FORMAT;

/**
 * 댓글 조회용 DTO.
 * 질문글 및 답변글에서 해당 DTO를 사용해서 댓글에 대한 화면구현을 한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDTO implements Comparable<CommentDTO> {

    private long commentId;
    private long authorId;
    private String authorName;
    @DateTimeFormat(pattern = STRING_DATE_TIME_FORMAT)
    private LocalDateTime createdDate;
    private String content;
    private Long parentCommentId;
    private long parentPostId;
    private boolean deleted;
    private List<CommentDTO> children = new ArrayList<>();

    @Builder
    @QueryProjection
    public CommentDTO(long commentId,
                      long authorId,
                      String authorName,
                      LocalDateTime createdDate,
                      String content,
                      Long parentCommentId,
                      long parentPostId,
                      boolean deleted) {
        this.commentId = commentId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.createdDate = createdDate;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.parentPostId = parentPostId;
        this.deleted = deleted;
    }

    /**
     * 코멘트 DTO의 리스트를 매개변수로 받아서 부모자식 관계로 조립해서 반환함
     * 부모 코멘트는 자식 코멘트보다 먼저 작성되는 점을 이용해서 생성일로 정렬한 다음,
     * 맵에다가 넣어서 계층구조를 조립한다
     *
     * @param originalComments 계층구조 없이 선형 리스트에 담겨있는 댓글 리스트
     * @return 계층구조로 조립된 상태의 새로운 댓글 리스트인 newComments를 반환한다
     */
    public static List<CommentDTO> assemble(List<CommentDTO> originalComments) {
        Map<Long, CommentDTO> commentMap = new HashMap<>();
        List<CommentDTO> newComments = new ArrayList<>();

        originalComments.sort(CommentDTO::compareTo);

        for (CommentDTO comment : originalComments) {
            commentMap.put(comment.getCommentId(), comment);
            //최상위에 위치한 댓글이면 newComments에 넣는다
            if (comment.getParentCommentId() == null) {
                newComments.add(comment);
            } else {//자식 코멘트인 경우
                //맵에서 부모 코멘트를 찾고, addChild()로 자식을 부모에게 전달한다
                CommentDTO parentComment = commentMap.get(comment.getParentCommentId());
                if (parentComment != null) {
                    parentComment.addChild(comment);
                }
            }
        }
        //최상위 댓글도 정렬해줌
        newComments.sort(CommentDTO::compareTo);
        return newComments;
    }

    /**
     * 엔티티에서 DTO로 변환함
     */
    public static CommentDTO from(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.commentId = comment.getId();
        commentDTO.authorId = comment.getAuthor().getId();
        commentDTO.authorName = comment.getAuthor().getNickname();
        commentDTO.createdDate = comment.getCreatedDate();
        commentDTO.content = comment.getContent();
        commentDTO.parentCommentId = comment.getParentComment() == null ? null : comment.getParentComment().getId();
        commentDTO.parentPostId = comment.getParentPost().getId();
        return commentDTO;
    }

    /**
     * 삭제된 코멘트인 경우 내용과 작성자 정보를 숨긴다
     */
    public void blur() {
        if (!deleted) {
            return;
        }
        content = "";
        authorName = "";
        authorId = 0L;
    }

    /**
     * 자식 댓글 DTO를 추가하는 메서드.
     *
     * @param child 자식 댓글 DTO
     */
    private void addChild(CommentDTO child) {
        children.add(child);
    }

    /**
     * CommentDTO를 생성일로 비교하는 메서드. <br>
     * this에 해당하는 댓글객체가 비교대상보다 이전에 작성되었다면 -1을 리턴함(작다고 표현함)
     *
     * @param other 비교대상
     * @return 비교 결과.
     */
    @Override
    public int compareTo(CommentDTO other) {
        LocalDateTime left = this.getCreatedDate();
        LocalDateTime right = other.getCreatedDate();

        if (left.isBefore(right)) {
            return -1;
        } else if (left.isEqual(right)) {
            return 0;
        } else {
            return 1;
        }
    }
}
