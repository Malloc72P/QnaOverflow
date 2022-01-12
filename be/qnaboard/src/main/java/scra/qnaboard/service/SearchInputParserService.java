package scra.qnaboard.service;

import org.springframework.stereotype.Service;
import scra.qnaboard.web.dto.question.search.ParsedSearchQuestionDTO;
import scra.qnaboard.web.dto.question.search.SearchQuestionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SearchInputParserService {

    private final List<String> groupNames = new ArrayList<>();

    private final String groupNameScore = "score";
    private final String groupNameAnswers = "answers";
    private final String groupNameUser = "user";
    private final String groupNameTags = "tags";
    private final String groupNameTitle = "title";
    private final String regex = "(?<score>score:[+-]?[0-9]{1,})|" +
            "(?<answers>answers:[0-9]{1,})|" +
            "(?<user>user:[0-9]{1,})|" +
            "(?<tags>\\[[a-z,A-Z,0-9,ㄱ-ㅎ,ㅏ-ㅣ,가-힣,+-]{1,}\\])|" +
            "(?<title>\\\"[a-z,A-Z,0-9,ㄱ-ㅎ,ㅏ-ㅣ,가-힣,+-]{1,}\\\")";

    private final Pattern pattern;

    public SearchInputParserService() {
        this.pattern = Pattern.compile(regex);
        groupNames.add(groupNameScore);
        groupNames.add(groupNameTitle);
        groupNames.add(groupNameUser);
        groupNames.add(groupNameAnswers);
        groupNames.add(groupNameTags);
    }

    public ParsedSearchQuestionDTO parse(SearchQuestionDTO searchDTO) {
        ParsedSearchQuestionDTO parsedQuestionSearchDTO = new ParsedSearchQuestionDTO();
        String searchInput = searchDTO.getSearchInput();

        Matcher matcher = pattern.matcher(searchInput);
        while (matcher.find()) {
            updateDtoByMatcher(parsedQuestionSearchDTO, matcher);
        }
        return parsedQuestionSearchDTO;
    }

    private void updateDtoByMatcher(ParsedSearchQuestionDTO parsedQuestionSearchDTO, Matcher matcher) {
        for (String groupName : groupNames) {
            String group = matcher.group(groupName);
            if (group != null) {
                updateDtoByGroup(parsedQuestionSearchDTO, groupName, group);
            }
        }
    }

    private void updateDtoByGroup(ParsedSearchQuestionDTO parsedQuestionSearchDTO, String groupName, String group) {
        switch (groupName) {
            case groupNameUser:
                //user:516389
                long authorId = Long.parseLong(group.substring(5));
                parsedQuestionSearchDTO.setAuthorId(authorId);
                break;
            case groupNameScore:
                //score:1
                long score = Long.parseLong(group.substring(6));
                parsedQuestionSearchDTO.setScore(score);
                break;
            case groupNameAnswers:
                //answers:1
                long answers = Long.parseLong(group.substring(8));
                parsedQuestionSearchDTO.setAnswers(answers);
                break;
            case groupNameTags:
                //[asdf]
                String tag = group.substring(1, group.length() - 1);
                parsedQuestionSearchDTO.addTag(tag);
                break;
            case groupNameTitle:
                //"asdf"
                String title = group.substring(1, group.length() - 1);
                parsedQuestionSearchDTO.setTitle(title);
                break;
        }
    }
}
