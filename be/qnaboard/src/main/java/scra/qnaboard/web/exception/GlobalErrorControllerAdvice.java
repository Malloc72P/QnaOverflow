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
import scra.qnaboard.web.dto.ErrorDTO;

import java.util.Locale;

@RequiredArgsConstructor
@ControllerAdvice("scra.qnaboard.web.controller")
public class GlobalErrorControllerAdvice {

    private final MessageSource messageSource;

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

    private void updateModelByException(Model model, Locale locale,
                                        String titleCode, String reasonCode, String descriptionCode) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .title(messageSource.getMessage(titleCode, null, locale))
                .reason(messageSource.getMessage(reasonCode, null, locale))
                .description(messageSource.getMessage(descriptionCode, null, locale))
                .build();

        model.addAttribute("error", errorDTO);
    }

}
