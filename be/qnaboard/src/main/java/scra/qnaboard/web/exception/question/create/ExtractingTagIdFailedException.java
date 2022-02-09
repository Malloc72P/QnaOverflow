package scra.qnaboard.web.exception.question.create;

import scra.qnaboard.service.exception.DescriptionMessageCodeSupplier;

public class ExtractingTagIdFailedException extends RuntimeException implements DescriptionMessageCodeSupplier {
    private static final String ERROR_MESSAGE = "문자열에서 태그 아이디를 추출하는데 실패했습니다 : ";

    public ExtractingTagIdFailedException(String tags) {
        super(ERROR_MESSAGE + tags);
    }

    @Override
    public String descriptionMessageCode() {
        return "ui.error.page-desc-tag-extraction-failed";
    }
}
