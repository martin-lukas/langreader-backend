package dev.mlukas.langreader.security;

import javax.validation.constraints.NotBlank;

record LoginRequest(@NotBlank String username, @NotBlank String password) {}
