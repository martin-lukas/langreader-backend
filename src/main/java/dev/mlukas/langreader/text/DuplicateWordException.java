package dev.mlukas.langreader.text;

public class DuplicateWordException extends RuntimeException {
    public DuplicateWordException(String message) {
        super(message);
    }
}
