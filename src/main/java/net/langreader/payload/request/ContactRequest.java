package net.langreader.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class ContactRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}