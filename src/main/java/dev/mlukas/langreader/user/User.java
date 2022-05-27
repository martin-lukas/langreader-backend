package dev.mlukas.langreader.user;

import com.google.common.base.MoreObjects;
import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.text.Text;
import dev.mlukas.langreader.text.Word;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username = "";

    private String password = "";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chosen_lang_id")
    private @Nullable Language chosenLang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "native_lang_id")
    private Language nativeLang = Language.DEFAULT_LANGUAGE;

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

    private boolean enabled = true;

    public User() {
        // For JPA purposes
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public User(UserDetails userDetails) {
        this(userDetails.getUsername(), userDetails.getPassword(), userDetails.isEnabled());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
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

    public void addLanguage(Language newLang) {
        langs.add(newLang);
    }

    public void removeLanguage(Language lang) {
        langs.remove(lang);
    }

    public @Nullable Language getChosenLang() {
        return chosenLang;
    }

    public void setChosenLang(@Nullable Language chosenLang) {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // METHODS FOR USERSERVICE

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public @Nullable Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // ========================

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && enabled == user.enabled && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, enabled);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("enabled", enabled)
                .toString();
    }
}
