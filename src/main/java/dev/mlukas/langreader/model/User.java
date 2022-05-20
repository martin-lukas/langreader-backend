package dev.mlukas.langreader.model;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Nonnull
    private String username;
    @Nonnull
    @Column(name = "passwd")
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_langs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lang_id")
    )
    private List<Language> langs;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chosen_lang_id")
    private Language chosenLang;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "native_lang_id")
    private Language nativeLang;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Word> words;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Text> texts;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addLanguage(Language newLang) {
        langs.add(newLang);
    }

    public void removeLanguage(Language lang) {
        langs.remove(lang);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Language> getLangs() {
        return langs;
    }

    public void setLangs(List<Language> langs) {
        this.langs = langs;
    }

    public Language getChosenLang() {
        return chosenLang;
    }

    public void setChosenLang(Language chosenLang) {
        this.chosenLang = chosenLang;
    }

    public Language getNativeLang() {
        return nativeLang;
    }

    public void setNativeLang(Language nativeLang) {
        this.nativeLang = nativeLang;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username) && password.equals(user.password)
                && Objects.equals(langs, user.langs) && Objects.equals(chosenLang, user.chosenLang)
                && Objects.equals(nativeLang, user.nativeLang) && Objects.equals(words, user.words)
                && Objects.equals(texts, user.texts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, langs, chosenLang, nativeLang, words, texts);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("password", password)
                .add("langs", langs)
                .add("chosenLang", chosenLang)
                .add("nativeLang", nativeLang)
                .add("words", words)
                .add("texts", texts)
                .toString();
    }
}
