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
import scra.qnaboard.service.exception.DeleteFailedException;
import scra.qnaboard.service.exception.EditFailedException;
import scra.qnaboard.service.exception.EntityNotFoundException;
import scra.qnaboard.service.exception.vote.DuplicateVoteException;
import scra.qnaboard.dto.error.ApiErrorDTO;

import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice("scra.qnaboard.web.api")
public class ApiGlobalErrorControllerAdvice {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NoSessionUserException.class)
    public ApiErrorDTO notLoggedInUser(NoSessionUserException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateVoteException.class)
    public ApiErrorDTO duplicateVote(DuplicateVoteException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({DeleteFailedException.class})
    public ApiErrorDTO deleteFailed(DeleteFailedException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(EditFailedException.class)
    public ApiErrorDTO editFailed(EditFailedException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiErrorDTO entityNotFound(EntityNotFoundException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiErrorDTO bindingErrors(MethodArgumentNotValidException exception, Locale locale) {
        String errorDescription = exception.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("\r\n"));

        return ApiErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .description(errorDescription)
                .build();
    }
}
