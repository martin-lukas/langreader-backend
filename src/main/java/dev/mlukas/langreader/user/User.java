package dev.mlukas.langreader.user;

import com.google.common.base.MoreObjects;
import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.language.NoChosenLanguageException;
import dev.mlukas.langreader.text.Text;
import dev.mlukas.langreader.text.Word;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username = "";

    // Column names passwd to avoid clash with keyword
    @Column(name = "passwd")
    private String password = "";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chosen_lang_id")
    private @Nullable Language chosenLang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "native_lang_id")
    private @Nullable Language nativeLang;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_langs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lang_id")
    )
    private List<Language> langs = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Word> words = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Text> texts = new ArrayList<>();

    public User() {
        // For JPA purposes
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (roles.contains(role)) {
            throw new UserAlreadyHasRoleException("User %s already has the role %s".formatted(username, role.getType()));
        }
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public List<Language> getLangs() {
        return langs;
    }

    public void setLangs(List<Language> langs) {
        this.langs = langs;
    }

    public void addLanguage(Language newLang) {
        langs.add(newLang);
    }

    public void removeLanguage(Language lang) {
        langs.remove(lang);
    }

    public Language getChosenLang() {
        if (chosenLang == null) {
            throw new NoChosenLanguageException("User '%s' hasn't chosen a language yet.".formatted(username));
        }
        return chosenLang;
    }

    public void setChosenLang(@Nullable Language chosenLang) {
        this.chosenLang = chosenLang;
    }

    public Language getNativeLang() {
        if (nativeLang == null) {
            throw new IllegalStateException("User '%s' is invalid - doesn't have a native language.".formatted(username));
        }
        return nativeLang;
    }

    public void setNativeLang(@Nullable Language nativeLang) {
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
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username) && password.equals(user.password) && roles.equals(user.roles) && Objects.equals(langs, user.langs) && Objects.equals(chosenLang, user.chosenLang) && Objects.equals(nativeLang, user.nativeLang) && words.equals(user.words) && texts.equals(user.texts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, langs, roles, chosenLang, nativeLang, words, texts);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("password", password)
                .add("roles", roles)
                .add("langs", langs)
                .add("chosenLang", chosenLang)
                .add("nativeLang", nativeLang)
                .toString();
    }
}
