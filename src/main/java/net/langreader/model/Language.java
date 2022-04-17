package net.langreader.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "langs")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String fullName;

    public int getId() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return id == language.id && Objects.equals(code, language.code) && Objects.equals(fullName, language.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, fullName);
    }
}
