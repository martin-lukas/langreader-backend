package dev.mlukas.langreader.text;

import javax.validation.constraints.NotBlank;

public record TextFromUrlRequest(@NotBlank String title, @NotBlank String url) {}
