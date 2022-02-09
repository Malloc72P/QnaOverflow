package scra.qnaboard.dto.question.edit;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.dto.tag.search.TagSimpleDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 질문글과 태그 정보만 가지고 있는 DTO.
 * 질문글 수정 페이지를 위해 필요함.
 * 제목, 내용, 태그 정보를 수정해야 하기 때문임.
 */
@Getter
@NoArgsConstructor
public class QuestionWithTagDTO {

    private String title;
    private String content;
    private List<TagSimpleDTO> tags;

    @Builder
    public QuestionWithTagDTO(String title, String content, List<TagSimpleDTO> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    /**
     * 질문글 -> DTO 변환 메서드
     * question.getQuestionTags()이랑 QuestionTag::getTag()로 인해 Lazy Loading이 발생함에 주의할 것.
     */
    public static QuestionWithTagDTO from(Question question) {
        QuestionWithTagDTO dto = new QuestionWithTagDTO();

        dto.title = question.getTitle();
        dto.content = question.getContent();

        dto.tags = question.getQuestionTags().stream()
                .map(QuestionTag::getTag)
                .map(TagSimpleDTO::from)
                .collect(Collectors.toList());

        return dto;
    }
}
