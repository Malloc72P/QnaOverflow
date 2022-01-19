package scra.qnaboard.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import scra.qnaboard.web.dto.ErrorDTO;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private static final String MESSAGE_CODE_TITLE = "ui.error.page-title";
    private static final String MESSAGE_CODE_REASON = "ui.error.page-reason-";
    private static final String MESSAGE_CODE_DESC = "ui.error.page-desc-";

    private final MessageSource messageSource;

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerNotFound(Model model, Locale locale) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .title(messageSource.getMessage(MESSAGE_CODE_TITLE + "404", null, locale))
                .reason(messageSource.getMessage(MESSAGE_CODE_REASON + "404", null, locale))
                .description(messageSource.getMessage(MESSAGE_CODE_DESC + "404", null, locale))
                .build();
        model.addAttribute("error", errorDTO);
        return "/error/error-page";
    }

}
