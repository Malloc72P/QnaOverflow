package scra.qnaboard.dto.question;

import scra.qnaboard.web.exception.question.create.ExtractingTagIdFailedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionTagForm {

    protected String tags;

    public List<Long> extractTagIds() {
        if (tags.equals("")) {
            return new ArrayList<>();
        }
        try {
            return Arrays.stream(tags.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException numberFormatException) {
            throw new ExtractingTagIdFailedException(tags);
        }
    }
}
