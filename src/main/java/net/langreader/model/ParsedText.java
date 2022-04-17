package net.langreader.model;

import com.google.common.base.MoreObjects;

import java.util.List;

public class ParsedText {
    private String title;
    private List<List<Token>> paragraphs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<List<Token>> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<List<Token>> paragraphs) {
        this.paragraphs = paragraphs;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("paragraphs", paragraphs)
                .toString();
    }
}
