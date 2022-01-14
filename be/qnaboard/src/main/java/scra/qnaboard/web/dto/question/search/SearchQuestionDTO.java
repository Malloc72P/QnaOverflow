package scra.qnaboard.web.dto.question.search;

import lombok.Getter;

import java.util.Objects;

@Getter
public class SearchQuestionDTO {

    private String searchInput = "";

    public SearchQuestionDTO(String searchInput) {
        this.searchInput = searchInput == null ? "" : searchInput;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchQuestionDTO that = (SearchQuestionDTO) o;
        return Objects.equals(getSearchInput(), that.getSearchInput());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSearchInput());
    }
}
