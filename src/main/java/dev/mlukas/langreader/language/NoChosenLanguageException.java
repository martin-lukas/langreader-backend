package dev.mlukas.langreader.language;

public class NoChosenLanguageException extends RuntimeException {
    public NoChosenLanguageException(String username) {
        super("User '%s' hasn't chosen a language yet.".formatted(username));
    }
}
