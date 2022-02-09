package scra.qnaboard.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import scra.qnaboard.configuration.auth.NoSessionUserException;
import scra.qnaboard.service.exception.DeleteFailedException;
import scra.qnaboard.service.exception.EditFailedException;
import scra.qnaboard.service.exception.EntityNotFoundException;
import scra.qnaboard.dto.error.ErrorDTO;
import scra.qnaboard.web.exception.question.create.ExtractingTagIdFailedException;

import java.util.Locale;

/**
 * 컨트롤러에서 발생하는 예외를 처리하는 ControllerAdvice
 */
@RequiredArgsConstructor
@ControllerAdvice("scra.qnaboard.web.controller")
public class GlobalErrorControllerAdvice {

    private final MessageSource messageSource;

    /**
     * 엔티티를 못찾은 경우에 대한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public String entityNotFound(EntityNotFoundException exception, Model model, Locale locale) {
        updateModelByException(model,
                locale,
                "ui.error.page-title-entity-not-found",
                "ui.error.page-reason-entity-not-found",
                exception.descriptionMessageCode());
        return "error/error-page";
    }

    /**
     * 엔티티를 못찾은 경우에 대한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(DeleteFailedException.class)
    public String deleteFailed(DeleteFailedException exception, Model model, Locale locale) {
        updateModelByException(model,
                locale,
                "ui.error.page-title-entity-delete-failed",
                "ui.error.page-reason-entity-delete-failed",
                exception.descriptionMessageCode());
        return "error/error-page";
    }

    /**
     * 수정요청에 실패한 경우 발생하는 예외
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(EditFailedException.class)
    public String editFailed(EditFailedException exception, Model model, Locale locale) {
        updateModelByException(model,
                locale,
                "ui.error.page-title-entity-edit-failed",
                "ui.error.page-reason-entity-edit-failed",
                exception.descriptionMessageCode());
        return "error/error-page";
    }

    /**
     * 인증오류에 대한 예외처리
     * 로그인 하지 않고 로그인이 필요한 작업을 요청한 경우에 대해 처리한다
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NoSessionUserException.class)
    public String notLoggedInUser(NoSessionUserException exception, Model model, Locale locale) {
        updateModelByException(model,
                locale,
                "ui.error.page-title-no-log-in",
                "ui.error.page-reason-no-log-in",
                exception.descriptionMessageCode());
        return "error/error-page";
    }

    /**
     * 잘못된 태그입력에 대한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExtractingTagIdFailedException.class)
    public String tagExtractionFailed(ExtractingTagIdFailedException exception, Model model, Locale locale) {
        updateModelByException(model,
                locale,
                "ui.error.page-title-tag-extraction-failed",
                "ui.error.page-reason-tag-extraction-failed",
                exception.descriptionMessageCode());
        return "error/error-page";
    }

    /**
     * 에러페이지 화면구현에 필요한 문자열을 메세지소스에서 꺼낸 다음 모델에 담는다
     */
    private void updateModelByException(Model model, Locale locale,
                                        String titleCode, String reasonCode, String descriptionCode) {
        //에러페이지에 필요한 DTO 생성
        ErrorDTO errorDTO = ErrorDTO.builder()
                .title(messageSource.getMessage(titleCode, null, locale))
                .reason(messageSource.getMessage(reasonCode, null, locale))
                .description(messageSource.getMessage(descriptionCode, null, locale))
                .build();
        //모델에 DTO를 넣는다
        model.addAttribute("error", errorDTO);
    }

}
