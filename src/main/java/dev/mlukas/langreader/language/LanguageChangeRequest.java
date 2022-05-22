package dev.mlukas.langreader.language;

import javax.validation.constraints.NotBlank;

public record LanguageChangeRequest(@NotBlank String code, @NotBlank String fullName) {}
