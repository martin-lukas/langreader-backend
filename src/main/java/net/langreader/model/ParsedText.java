package net.langreader.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString
public class ParsedText {
    private String title;
    private List<List<Token>> paragraphs;
}
