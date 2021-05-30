package net.langreader.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @ToString
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String username;
    @NonNull
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
}
