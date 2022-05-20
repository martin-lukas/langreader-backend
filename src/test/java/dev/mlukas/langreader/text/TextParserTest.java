package dev.mlukas.langreader.text;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TextParserTest {
    @Test
    void parseTextTest() {
        ParsedText result = TextParser.parseText(
                new Text(0, "Title", "Hello.\nThis is second paragraph.\nThird one.", null, null));
        List<List<Token>> paragraphs = result.getParagraphs();
        assertEquals(3, paragraphs.size(), "Wrong number of paragraphs.");
        assertEquals(2, paragraphs.get(0).size(), "Wrong number of tokens in 1st par.");
        assertEquals(8, paragraphs.get(1).size(), "Wrong number of tokens in 2st par.");
        assertEquals(4, paragraphs.get(2).size(), "Wrong number of tokens in 3st par.");
    }

    @Test
    void parseEmptyTextTest() {
        ParsedText result = TextParser.parseText(new Text(0, "", "", null, null));
        assertEquals(0, result.getParagraphs().size(), "There should be no paragraph in an empty string.");
    }

    @Test
    void parseTextWithSpecialCharsTest() {
        ParsedText result = TextParser.parseText(
                new Text(0, "", "This; (\"btw\". [non-idea]) I'm okay?", null, null));
        assertEquals(1, result.getParagraphs().size(), "There should be exactly 1 paragraph.");
        assertEquals(15, result.getParagraphs().get(0).size(), "There should be exactly 15 tokens.");
    }

    @Test
    void parseTextWithNumbersTest() {
        ParsedText result = TextParser.parseText(new Text(0, "", "This Plan8 is 789 points.", null, null));
        List<List<Token>> paragraphs = result.getParagraphs();
        assertEquals(1, paragraphs.size(), "There should be exactly 1 paragraph.");
        List<Token> tokens = paragraphs.get(0);
        assertEquals(11, tokens.size(), "There should be exactly 10 tokens.");
        assertEquals("Plan", tokens.get(2).getValue(), "The 3rd token doesn't fit.");
        assertNull(tokens.get(6).getType(), "The 7th token shouldn't count as a word.");
    }
}