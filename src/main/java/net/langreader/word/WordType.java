package net.langreader.word;

public enum WordType {
    KNOWN,
    STUDIED,
    IGNORED,
    UNKNOWN;

    public static boolean isValid(WordType newWordType) {
        return newWordType != null && newWordType != UNKNOWN;
    }
}
