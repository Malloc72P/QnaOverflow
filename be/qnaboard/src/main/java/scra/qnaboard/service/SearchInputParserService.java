package scra.qnaboard.service;

import org.springframework.stereotype.Service;
import scra.qnaboard.dto.question.search.ParsedSearchQuestionDTO;
import scra.qnaboard.dto.question.search.SearchQuestionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SearchQuestionDTO를 ParsedSearchQuestionDTO로 파싱하는 서비스.
 * 정규표현식을 가지고 문자열로 된 검색어를 DTO로 파싱함
 * 검색어는 5가지 유형으로 되어있다(추천점수, 답변글개수, 작성자아이디, 태그, 제목)
 * 각각의 유형에 대해 그룹을 만들어 놓았고, 이러한 그룹의 이름을 모아놓은 리스트가 바로 groupNames이다.
 */
@Service
public class SearchInputParserService {
    //그룹의 이름을 모아놓은 리스트.
    private final List<String> groupNames = new ArrayList<>();
    //그룹의 이름들.
    private final String groupNameScore = "score";
    private final String groupNameAnswers = "answers";
    private final String groupNameUser = "user";
    private final String groupNameTags = "tags";
    private final String groupNameTitle = "title";
    //ParsedSearchQuestionDTO로 파싱할 때 쓰는 정규표현식
    private final String regex = "(?<score>score:[+-]?[0-9]{1,})|" +
            "(?<answers>answers:[0-9]{1,})|" +
            "(?<user>user:[0-9]{1,})|" +
            "(?<tags>\\[[a-z,A-Z,0-9,ㄱ-ㅎ,ㅏ-ㅣ,가-힣, +-]{1,}\\])|" +
            "(?<title>\\\"[a-z,A-Z,0-9,ㄱ-ㅎ,ㅏ-ㅣ,가-힣, +-]{1,}\\\")";
    //정규표현식으로 만든 패턴 객체
    private final Pattern pattern;

    public SearchInputParserService() {
        this.pattern = Pattern.compile(regex);
        groupNames.add(groupNameScore);
        groupNames.add(groupNameTitle);
        groupNames.add(groupNameUser);
        groupNames.add(groupNameAnswers);
        groupNames.add(groupNameTags);
    }

    /**
     * 검색어를 파싱해서 DTO로 만들고 반환함
     *
     * @param searchDTO 파싱되지 않은 검색어
     * @return 파싱된 검색어 DTO
     */
    public ParsedSearchQuestionDTO parse(SearchQuestionDTO searchDTO) {
        //검색어 DTO 생성
        ParsedSearchQuestionDTO parsedQuestionSearchDTO = new ParsedSearchQuestionDTO();
        //파싱되지 않은 검색어를 꺼냄.
        String searchInput = searchDTO.getSearchInput();
        //패턴을 통해 Matcher 객체를 생성함
        Matcher matcher = pattern.matcher(searchInput);
        //검색어를 전부 찾을때 까지 반복함
        while (matcher.find()) {
            //찾은 검색어를 가지고 파싱된 검색어 DTO를 업데이트함
            updateDtoByMatcher(parsedQuestionSearchDTO, matcher);
        }
        //파싱된 검색어 DTO를 반환함
        return parsedQuestionSearchDTO;
    }

    /**
     * 찾은 검색어를 가지고 파싱된 검색어 DTO를 업데이트함
     */
    private void updateDtoByMatcher(ParsedSearchQuestionDTO parsedQuestionSearchDTO, Matcher matcher) {
        //매칭된 그룹이 존재하는지 확인.
        for (String groupName : groupNames) {
            String group = matcher.group(groupName);
            //매칭된 그룹이 존재한다면, DTO를 업데이트함
            if (group != null) {
                updateDtoByGroup(parsedQuestionSearchDTO, groupName, group);
            }
        }
    }

    /**
     * 찾은 검색어를 가지고 파싱된 검색어 DTO를 업데이트함
     *
     * @param parsedQuestionSearchDTO 검색어 DTO
     * @param groupName               찾은 검색어가 소속한 그룹의 이름
     * @param group                   찾은 검색어(ex: "user:516389")
     */
    private void updateDtoByGroup(ParsedSearchQuestionDTO parsedQuestionSearchDTO, String groupName, String group) {
        //그룹 이름에 따라 각각 다른 방식으로 검색어를 추출해서 DTO에 집어넣는다.
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
