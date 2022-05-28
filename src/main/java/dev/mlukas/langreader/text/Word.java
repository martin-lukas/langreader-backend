package dev.mlukas.langreader.text;

import com.google.common.base.MoreObjects;
import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.security.User;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable Long id;

    private String value = "";

    @Enumerated
    @Column(columnDefinition = "int", name = "type_id")
    private WordType type = WordType.UNKNOWN;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lang_id")
    private @Nullable Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private @Nullable User user;

    @SuppressWarnings("unused")
    public Word() {
        // For JPA purposes
    }

    public Word(@Nullable Long id, String value, WordType type, Language language, User user) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.language = language;
        this.user = user;
    }

    public @Nullable Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public @Nullable Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public @Nullable User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("value", value)
                .add("type", type)
                .add("language", language)
                .add("user", user)
                .toString();
    }
}
