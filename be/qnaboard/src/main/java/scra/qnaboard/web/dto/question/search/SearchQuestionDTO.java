package scra.qnaboard.web.dto.question.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchQuestionDTO {

    private String searchInput;
    private int pageNumber;
    private int pageSize;
}
