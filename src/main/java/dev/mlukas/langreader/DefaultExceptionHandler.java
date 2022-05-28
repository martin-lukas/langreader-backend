package dev.mlukas.langreader;

import dev.mlukas.langreader.language.NoChosenLanguageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage accessDenied(AccessDeniedException exception) {
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED,
                "You're not authorized to view %s.".formatted(exception.getMessage()),
                LocalDateTime.now());
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage userNotFound(UsernameNotFoundException exception) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({NoChosenLanguageException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage noChosenLanguageYet(NoChosenLanguageException exception) {
        return new ErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY,
                exception.getMessage(),
                LocalDateTime.now()
        );
    }
}
