package scra.qnaboard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;
import scra.qnaboard.web.dto.question.search.ParsedSearchQuestionDTO;
import scra.qnaboard.web.dto.question.search.SearchQuestionDTO;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static scra.qnaboard.web.dto.question.search.ParsedSearchQuestionDTO.*;

class SearchInputServiceTest {

    private SearchInputParserService searchInputService;

    @BeforeEach
    void init() {
        searchInputService = new SearchInputParserService();
    }

    @Test
    @DisplayName("검색어에 대한 정규 표현식 패턴을 불러올 수 있어야 함")
    void testLoadRegex() {
        String regex = (String) ReflectionTestUtils.getField(searchInputService, "regex");
        Pattern pattern = (Pattern) ReflectionTestUtils.getField(searchInputService, "pattern");

        assertThat(StringUtils.hasText(regex)).isTrue();
        assertThat(pattern).isNotNull();
    }

    @Test
    @DisplayName("검색어를 정규표현식으로 검사한 다음, 새로운 DTO로 변환할 수 있어야 함")
    void testParse() {
        String[] testcases = {
                "user:516389 score:30 answers:10 \"asdf\" [angular] [javascript] ",
                "user:516389 score:30 answers:10 \"asdf\" ",
                "user:516389 score:30 answers:10",
                "user:516389 score:30",
                "user:516389",
                "",
                "\"asdf\"",
                "\"title-2\"",
                "[Angular] \"Title-2\"",
        };
        Object[][] expecteds = {
                {516389L, 30L, 10L, "asdf", 2},
                {516389L, 30L, 10L, "asdf", 0},
                {516389L, 30L, 10L, DEFAULT_TITLE, 0},
                {516389L, 30L, DEFAULT_ANSWERS, DEFAULT_TITLE, 0},
                {516389L, DEFAULT_SCORE, DEFAULT_ANSWERS, DEFAULT_TITLE, 0},
                {DEFAULT_AUTHOR_ID, DEFAULT_SCORE, DEFAULT_ANSWERS, DEFAULT_TITLE, 0},
                {DEFAULT_AUTHOR_ID, DEFAULT_SCORE, DEFAULT_ANSWERS, "asdf", 0},
                {DEFAULT_AUTHOR_ID, DEFAULT_SCORE, DEFAULT_ANSWERS, "title-2", 0},
                {DEFAULT_AUTHOR_ID, DEFAULT_SCORE, DEFAULT_ANSWERS, "Title-2", 1},
        };
        assertThat(testcases.length).isEqualTo(expecteds.length);

        for (int i = 0; i < testcases.length; i++) {
            String testcase = testcases[i];
            Object[] expected = expecteds[i];
            ParsedSearchQuestionDTO parse = searchInputService.parse(new SearchQuestionDTO(testcase));

            assertThat(parse).extracting(
                    ParsedSearchQuestionDTO::getAuthorId,
                    ParsedSearchQuestionDTO::getScore,
                    ParsedSearchQuestionDTO::getAnswers,
                    ParsedSearchQuestionDTO::getTitle
            ).containsExactly(
                    expected[0],
                    expected[1],
                    expected[2],
                    expected[3]
            );
            assertThat(parse.getTags().size()).isEqualTo(expected[4]);

        }
    }
}
