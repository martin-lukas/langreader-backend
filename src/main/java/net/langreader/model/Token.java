package net.langreader.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class Token {
    public String value;
    public WordType type;

    public boolean isValid(boolean isTypeNeeded) {
        return value != null && !value.isBlank() && (!isTypeNeeded || (WordType.isValid(type)));
    }
}
