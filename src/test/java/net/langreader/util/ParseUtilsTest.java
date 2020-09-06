package net.langreader.util;

import net.langreader.dto.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParseUtilsTest {
    @Test
    void parseTextTest() {
        List<List<Token>> result = ParseUtils.parseText("Hello.\nThis is second paragraph.\nThird one.");
        assertEquals(3, result.size(), "Wrong number of paragraphs.");
        assertEquals(2, result.get(0).size(), "Wrong number of tokens in 1st par.");
        assertEquals(8, result.get(1).size(), "Wrong number of tokens in 2st par.");
        assertEquals(4, result.get(2).size(), "Wrong number of tokens in 3st par.");
    }

    @Test
    void parseEmptyTextTest() {
        List<List<Token>> result = ParseUtils.parseText("");
        assertEquals(0, result.size(), "There should be no paragraph in an empty string.");
    }

    @Test
    void parseTextWithSpecialCharsTest() {
        List<List<Token>> result = ParseUtils.parseText("This; (\"btw\". [non-idea]) I'm okay?");
        assertEquals(1, result.size(), "There should be exactly 1 paragraph.");
        assertEquals(15, result.get(0).size(), "There should be exactly 15 tokens.");
    }

    @Test
    void parseTextWithNumbersTest() {
        List<List<Token>> result = ParseUtils.parseText("This Plan8 is 789 points.");
        assertEquals(1, result.size(), "There should be exactly 1 paragraph.");
        List<Token> tokens = result.get(0);
        assertEquals(11, tokens.size(), "There should be exactly 10 tokens.");
        assertEquals("Plan", tokens.get(2).getValue(), "The 3rd token doesn't fit.");
        assertNull(tokens.get(6).getType(), "The 7th token shouldn't count as a word.");
    }
}