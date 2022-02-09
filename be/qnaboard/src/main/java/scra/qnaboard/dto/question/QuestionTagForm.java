package scra.qnaboard.dto.question;

import scra.qnaboard.web.exception.question.create.ExtractingTagIdFailedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 태그 추가 및 수정을 위한 DTO.
 * 컨트롤러의 파라미터에서 사용하는 DTO에서 QuestionTagForm를 상속해서 사용한다.
 */
public class QuestionTagForm {

    protected String tags;

    /**
     * 문자열로 된 태그 아이디 목록에서 아이디를 추출한 다음 List에 넣고 반환함
     * ex: 문자열이 "asdf,qwer"로 되어 있다면 ["asdf", "qwer"]로 만들어서 반환한다
     * 변환에 실패하는 경우 ExtractingTagIdFailedException 예외를 발생시킨다
     */
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
