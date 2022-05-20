package dev.mlukas.langreader.util;

import dev.mlukas.langreader.model.ParsedText;
import dev.mlukas.langreader.model.Token;
import dev.mlukas.langreader.model.Text;
import dev.mlukas.langreader.model.WordType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextParser {
    private static final String NON_WORD_CHARS = "0-9.,!?:;(){}\\[\\]<>\"'„“”‘’…¿¡=@#~";
    private static final Pattern REGEXP = Pattern.compile(String.format(
            "([%s]*)([^%s]+)([%s]*)",
            NON_WORD_CHARS,
            NON_WORD_CHARS,
            NON_WORD_CHARS));

    public static ParsedText parseText(Text text) {
        ParsedText parsedText = new ParsedText();
        parsedText.setTitle(text.getTitle());
        List<List<Token>> tokenizedParagraphs = text.getText().lines()
                .map(paragraph -> parseParagraph(paragraph.trim()))
                .collect(Collectors.toList());
        parsedText.setParagraphs(tokenizedParagraphs);
        return parsedText;
    }

    private static List<Token> parseParagraph(String paragraph) {
        String[] strings = paragraph.split("\\s+");
        List<Token> tokens = new ArrayList<>();

        Arrays.stream(strings).forEach(string -> {
            tokens.addAll(parseString(string));
            tokens.add(new Token(" ", null));
        });
        tokens.remove(tokens.size() - 1); // remove the last space token
        return tokens;
    }

    private static List<Token> parseString(String string) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = REGEXP.matcher(string);
        if (matcher.matches()) {
            for (int i = 1; i <= 3; i++) {
                String match = matcher.group(i);
                if (!match.isEmpty()) { // matches[2] contains the word itself
                    tokens.add(new Token(match, (i == 2) ? WordType.UNKNOWN : null));
                }
            }
        } else {
            tokens.add(new Token(string, null));
        }

        return tokens;
    }
}
