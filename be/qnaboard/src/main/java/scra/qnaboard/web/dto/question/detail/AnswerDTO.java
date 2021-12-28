package scra.qnaboard.web.dto.question.detail;

import lombok.Getter;
import scra.qnaboard.domain.entity.post.Answer;

import java.time.LocalDateTime;

/**
 * 답변 게시글을 위한 DTO
 * 날짜 포맷은 Thymeleaf에서 직접 적용함(프래그먼트를 사용하다보니 애너테이션의 포매터가 적용이 안된다!)
 * 프래그먼트에 localDateTime만 넘기고 DTO는 안넘겨서 애너테이션 정보가 전달되지 않는 모양이다
 * 바꾸고 싶으면 post-controller.html 프래그먼트에서 수정하자
 */

@Getter
public class AnswerDTO {

    private long answerId;
    private String content;
    private long voteScore;
    private LocalDateTime createdDate;
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
