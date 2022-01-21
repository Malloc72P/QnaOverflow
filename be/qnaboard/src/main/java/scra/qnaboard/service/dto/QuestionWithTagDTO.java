package scra.qnaboard.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.web.dto.tag.search.TagSimpleDTO;

import java.util.List;
import java.util.stream.Collectors;

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
