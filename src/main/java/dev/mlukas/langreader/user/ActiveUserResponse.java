package dev.mlukas.langreader.user;

import dev.mlukas.langreader.language.Language;

public record ActiveUserResponse(
        String username,
        Language chosenLang,
        Language nativeLang
) {
    public ActiveUserResponse (User user) {
        this(user.getUsername(), user.getChosenLang(), user.getNativeLang());
    }
}
