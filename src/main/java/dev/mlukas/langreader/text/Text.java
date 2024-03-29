package dev.mlukas.langreader.text;

import com.google.common.base.MoreObjects;
import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.security.User;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "texts")
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable Integer id;

    private String title = "";

    // TODO: Rename field to content.
    private String text = "";

    @OneToOne
    @JoinColumn(name = "lang_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Text() {
        // For JPA purposes
    }

    public Text(@Nullable Integer id, String title, String text, Language language, User user) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.language = language;
        this.user = user;
    }

    public @Nullable Integer getId() {
        return id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("text", text)
                .add("language", language)
                .add("user", user)
                .toString();
    }
}
