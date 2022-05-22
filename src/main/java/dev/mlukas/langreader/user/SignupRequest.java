package dev.mlukas.langreader.user;

import dev.mlukas.langreader.language.Language;

import javax.validation.constraints.NotBlank;

record SignupRequest(
    @NotBlank String username,
    @NotBlank String password,
    Language nativeLang
) {}
