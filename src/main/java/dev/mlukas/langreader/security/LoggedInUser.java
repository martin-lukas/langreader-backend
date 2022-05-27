package dev.mlukas.langreader.security;

import dev.mlukas.langreader.language.Language;
import org.checkerframework.checker.nullness.qual.Nullable;

public record LoggedInUser(
        String username,
        @Nullable Language chosenLang,
        Language nativeLang
) {
    public LoggedInUser(User user) {
        this(user.getUsername(), user.getChosenLang(), user.getNativeLang());
    }
}
