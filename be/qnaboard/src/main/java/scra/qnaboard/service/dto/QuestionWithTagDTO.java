package scra.qnaboard.service.dto;

import lombok.Getter;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.web.dto.tag.search.TagSimpleDTO;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuestionWithTagDTO {

    private String title;
    private String content;
    private List<TagSimpleDTO> tags;

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
