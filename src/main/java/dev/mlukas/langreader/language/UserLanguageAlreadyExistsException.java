package dev.mlukas.langreader.language;

public class UserLanguageAlreadyExistsException extends RuntimeException {
    public UserLanguageAlreadyExistsException(String username, String language) {
        super("The user '%s' already has the language '%s' in studied languages.".formatted(username, language));
    }
}
