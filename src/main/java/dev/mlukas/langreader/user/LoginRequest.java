package dev.mlukas.langreader.user;

import javax.validation.constraints.NotBlank;

record LoginRequest(@NotBlank String username, @NotBlank String password) {}
