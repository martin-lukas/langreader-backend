package net.langreader.dto;

import lombok.*;
import net.langreader.model.WordType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
public class Token {
    public String value;
    public WordType type;
}
