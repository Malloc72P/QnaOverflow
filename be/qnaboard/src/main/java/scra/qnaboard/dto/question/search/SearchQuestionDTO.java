package scra.qnaboard.dto.question.search;

import lombok.Getter;

import java.util.Objects;

/**
 * 질문글 검색을 위한 DTO
 * 사용자가 검색창에 입력한 내용 그대로가 searchInput에 담겨있다.
 */
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
