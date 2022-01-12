package scra.qnaboard.web.dto.question.search;

import lombok.Getter;

@Getter
public class SearchQuestionDTO {

    private String searchInput = "";

    public SearchQuestionDTO(String searchInput) {
        this.searchInput = searchInput == null ? "" : searchInput;
    }
}
