package dev.mlukas.langreader.text;

import javax.validation.constraints.NotBlank;

public record TextUpdateRequest(
        Integer id,
        @NotBlank String title,
        @NotBlank String text
) {}
