package dev.mlukas.langreader.text;

import com.google.common.base.MoreObjects;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Token {
    private String value;
    private @Nullable WordType type;

    public Token(String value, @Nullable WordType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public @Nullable WordType getType() {
        return type;
    }

    public void setType(WordType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("type", type)
                .toString();
    }
}
