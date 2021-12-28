package scra.qnaboard.web.dto.question.detail;

import lombok.Getter;
import scra.qnaboard.domain.entity.QuestionTag;
import scra.qnaboard.domain.entity.post.Question;
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
public class QuestionDetailDTO {

    private long questionId;
    private String title;
    private String content;
    private long voteScore;
    private List<AnswerDTO> answers = new ArrayList<>();
    private long viewCount;
    private LocalDateTime createdDate;
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
