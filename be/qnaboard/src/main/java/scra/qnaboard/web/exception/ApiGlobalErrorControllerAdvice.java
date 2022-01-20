package scra.qnaboard.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import scra.qnaboard.configuration.auth.NoSessionUserException;
import scra.qnaboard.service.exception.vote.DuplicateVoteException;
import scra.qnaboard.web.dto.ErrorDTO;

import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice("scra.qnaboard.web.api")
public class ApiGlobalErrorControllerAdvice {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NoSessionUserException.class)
    public ErrorDTO notLoggedInUser(NoSessionUserException exception, Locale locale) {
        return ErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .title(messageSource.getMessage("ui.error.page-title-no-log-in", null, locale))
                .reason(messageSource.getMessage("ui.error.page-reason-no-log-in", null, locale))
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateVoteException.class)
    public ErrorDTO duplicateVote(DuplicateVoteException exception, Locale locale) {
        return ErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .title(messageSource.getMessage("ui.error.page-title-no-log-in", null, locale))
                .reason(messageSource.getMessage("ui.error.page-reason-no-log-in", null, locale))
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO bindingErrors(MethodArgumentNotValidException exception, Locale locale) {
        String errorDescription = exception.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("\r\n"));

        return ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title(messageSource.getMessage("ui.error.page-title-no-log-in", null, locale))
                .reason(messageSource.getMessage("ui.error.page-reason-no-log-in", null, locale))
                .description(errorDescription)
                .build();
    }
}
