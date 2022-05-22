package dev.mlukas.langreader.user;

import javax.validation.constraints.NotBlank;

record SigninRequest(@NotBlank String username, @NotBlank String password) {}
