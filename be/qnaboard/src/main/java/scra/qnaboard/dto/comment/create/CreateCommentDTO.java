package scra.qnaboard.dto.comment.create;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCommentDTO {

    private Long parentCommentId;

    @NotBlank
    @Size(min = 6, max = 1000, message = "{Size.createCommentDTO.content}")
    private String content;

    @Builder
    public CreateCommentDTO(Long parentCommentId, String content) {
        this.parentCommentId = parentCommentId;
        this.content = content;
    }
}
