package scra.qnaboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import scra.qnaboard.service.exception.DeleteFailedException;
import scra.qnaboard.service.exception.EntityNotFoundException;
import scra.qnaboard.web.dto.ErrorDTO;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalErrorControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public String entityNotFound(EntityNotFoundException exception, Model model, Locale locale) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .title(messageSource.getMessage("ui.error.page-title-entity-not-found", null, locale))
                .reason(messageSource.getMessage("ui.error.page-reason-entity-not-found", null, locale))
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();

        model.addAttribute("error", errorDTO);
        return "/error/error-page";
    }

    @ExceptionHandler(DeleteFailedException.class)
    public String deleteFailed(DeleteFailedException exception, Model model, Locale locale) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .title(messageSource.getMessage("ui.error.page-title-entity-delete-failed", null, locale))
                .reason(messageSource.getMessage("ui.error.page-reason-entity-delete-failed", null, locale))
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();

        model.addAttribute("error", errorDTO);
        return "/error/error-page";
    }

}
