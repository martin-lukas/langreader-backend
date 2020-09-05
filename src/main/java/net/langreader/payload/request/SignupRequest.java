package net.langreader.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.langreader.model.Language;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor
public class SignupRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Language nativeLang;
}