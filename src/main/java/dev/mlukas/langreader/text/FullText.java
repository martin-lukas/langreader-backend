package dev.mlukas.langreader.text;

import dev.mlukas.langreader.language.Language;
import org.checkerframework.checker.nullness.qual.Nullable;

public record FullText(@Nullable Integer id, String title, String text, Language language) {
    public FullText(Text aText) {
        this(aText.getId(), aText.getTitle(), aText.getText(), aText.getLanguage());
    }
}
