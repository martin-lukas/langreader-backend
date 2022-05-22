package dev.mlukas.langreader.support;

import javax.validation.constraints.NotBlank;

public record ContactRequest(
    @NotBlank String email,
    @NotBlank String subject,
    @NotBlank String text
) { }