package scra.qnaboard.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
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
import scra.qnaboard.web.exception.question.create.ExtractingTagIdFailedException;

import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice("scra.qnaboard.web.api")
public class ApiGlobalErrorControllerAdvice {

    private final MessageSource messageSource;

    /**
     * 인증오류에 대한 예외처리
     * 로그인 하지 않고 로그인이 필요한 작업을 요청한 경우에 대해 처리한다
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NoSessionUserException.class)
    public ApiErrorDTO notLoggedInUser(NoSessionUserException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    /**
     * 중복투표에 대한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateVoteException.class)
    public ApiErrorDTO duplicateVote(DuplicateVoteException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    /**
     * 삭제요청에 실패에 대한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({DeleteFailedException.class})
    public ApiErrorDTO deleteFailed(DeleteFailedException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    /**
     * 수정요청에 실패에 대한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(EditFailedException.class)
    public ApiErrorDTO editFailed(EditFailedException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    /**
     * 엔티티를 못찾은 경우에 대한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiErrorDTO entityNotFound(EntityNotFoundException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }

    /**
     * 사용자의 잘못된 입력값으로 인한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiErrorDTO bindingErrors(MethodArgumentNotValidException exception, Locale locale) {
        //예외에서 발생한 모든 에러로 부터 디폴트 메세지를 꺼내서 하나의 문자열로 만든다.
        String errorDescription = exception.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("\r\n"));
        //API 에러 DTO를 생성해서 반환한다
        return ApiErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .description(errorDescription)
                .build();
    }

    /**
     * 잘못된 태그입력에 대한 예외를 처리함
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExtractingTagIdFailedException.class)
    public ApiErrorDTO tagExtractionFailed(ExtractingTagIdFailedException exception, Locale locale) {
        return ApiErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .description(messageSource.getMessage(exception.descriptionMessageCode(), null, locale))
                .build();
    }
}
