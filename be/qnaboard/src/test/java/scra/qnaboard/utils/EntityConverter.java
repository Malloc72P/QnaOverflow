package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;

import java.util.List;
import java.util.stream.Collectors;

public class EntityConverter {

    private EntityConverter() {
    }

    public static List<Long> getTagIds(Question question) {
        return question.getQuestionTags().stream()
                .map(QuestionTag::getTag)
                .map(Tag::getId)
                .collect(Collectors.toList());
    }
}
