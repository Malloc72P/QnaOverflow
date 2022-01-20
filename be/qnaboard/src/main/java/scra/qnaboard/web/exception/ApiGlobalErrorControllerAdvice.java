package scra.qnaboard.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import scra.qnaboard.configuration.auth.NoSessionUserException;
import scra.qnaboard.service.exception.DeleteFailedException;
import scra.qnaboard.service.exception.EditFailedException;
import scra.qnaboard.service.exception.EntityNotFoundException;
import scra.qnaboard.web.dto.ErrorDTO;

import java.util.Locale;

@RequiredArgsConstructor
@RestControllerAdvice("scra.qnaboard.web.api")
public class ApiGlobalErrorControllerAdvice {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NoSessionUserException.class)
    public ErrorDTO notLoggedInUser(NoSessionUserException exception, Locale locale) {
        return ErrorDTO.builder()
                .title(messageSource.getMessage("ui.error.page-title-no-log-in", null, locale))
                .reason(messageSource.getMessage("ui.error.page-reason-no-log-in", null, locale))
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }
}
