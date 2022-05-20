package dev.mlukas.langreader;

import dev.mlukas.langreader.language.NoChosenLanguageException;
import dev.mlukas.langreader.text.TextNotFoundException;
import dev.mlukas.langreader.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class, NoChosenLanguageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage userNotFound(RuntimeException exception) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(TextNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage textNotFound(RuntimeException exception) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST, "The text with the given ID was not found.", LocalDateTime.now());
    }
}
