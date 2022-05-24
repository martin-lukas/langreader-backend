package dev.mlukas.langreader.user;

import dev.mlukas.langreader.language.Language;

import java.util.Set;

public record ActiveUserResponse(
        String username,
        Set<Role> roles,
        Language chosenLang,
        Language nativeLang
) {
    public ActiveUserResponse (User user) {
        this(user.getUsername(), user.getRoles(), user.getChosenLang(), user.getNativeLang());
    }
}
