package net.langreader.text.parsing;

import lombok.*;
import net.langreader.word.WordType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class Token {
    public String value;
    public WordType type;

    public boolean isValid(boolean isTypeNeeded) {
        return value != null && !value.isBlank()
                && (!isTypeNeeded || (type != null && type != WordType.UNKNOWN));
    }
}
