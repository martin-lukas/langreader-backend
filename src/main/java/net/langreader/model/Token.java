package net.langreader.model;

import com.google.common.base.MoreObjects;

public class Token {
    public String value;
    public WordType type;

    public Token() {
    }

    public Token(String value, WordType type) {
        this.value = value;
        this.type = type;
    }

    public boolean isValid(boolean isTypeNeeded) {
        return value != null && !value.isBlank() && (!isTypeNeeded || (WordType.isValid(type)));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public WordType getType() {
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
