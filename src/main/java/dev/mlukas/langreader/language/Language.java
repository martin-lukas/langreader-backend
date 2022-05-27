package dev.mlukas.langreader.language;

import org.checkerframework.checker.nullness.qual.Nullable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "langs")
public class Language {
    public static final Language DEFAULT_LANGUAGE = new Language(14, "EN", "English");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable Integer id;

    private String code = "";

    private String fullName = "";

    public Language() {
        // For JPA purposes
    }

    public Language(@Nullable Integer id, String code, String fullName) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
    }

    public @Nullable Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return Objects.equals(id, language.id) && Objects.equals(code, language.code) && Objects.equals(fullName, language.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, fullName);
    }
}
