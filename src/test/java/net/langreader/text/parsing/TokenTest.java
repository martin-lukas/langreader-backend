package net.langreader.text.parsing;

import net.langreader.word.WordType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {
    @Test
    void isValid() {
        Token tokenWithType = new Token("aToken", WordType.STUDIED);
        assertTrue(tokenWithType.isValid(true));
        assertTrue(tokenWithType.isValid(false));
        Token tokenWithInvalidType = new Token("aToken", WordType.UNKNOWN);
        assertTrue(tokenWithInvalidType.isValid(false));
        assertFalse(tokenWithInvalidType.isValid(true));
        Token tokenWithoutType = new Token("aToken", null);
        assertFalse(tokenWithoutType.isValid(true));
        assertTrue(tokenWithoutType.isValid(false));
        Token tokenWithoutValue = new Token(null, WordType.STUDIED);
        assertFalse(tokenWithoutValue.isValid(false));
        assertFalse(tokenWithoutValue.isValid(true));
        Token emptyToken = new Token(null, null);
        assertFalse(emptyToken.isValid(true));
        assertFalse(emptyToken.isValid(false));
        Token blankToken = new Token("   ", null);
        assertFalse(blankToken.isValid(false));
        assertFalse(blankToken.isValid(true));
        Token blankTokenWithType = new Token("   ", WordType.KNOWN);
        assertFalse(blankTokenWithType.isValid(true));
        assertFalse(blankTokenWithType.isValid(false));
    }
}