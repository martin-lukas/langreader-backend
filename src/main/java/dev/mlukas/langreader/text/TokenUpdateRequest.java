package dev.mlukas.langreader.text;

import javax.validation.constraints.NotBlank;

public record TokenUpdateRequest(@NotBlank String value, WordType type) {}
