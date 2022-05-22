package dev.mlukas.langreader.text;

import javax.validation.constraints.NotBlank;

public record TextCreateRequest(
        @NotBlank String title,
        @NotBlank String text
) {}
