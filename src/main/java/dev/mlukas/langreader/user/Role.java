package dev.mlukas.langreader.user;

import com.google.common.base.MoreObjects;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RoleType type = RoleType.USER;

    public Role() {
        // For JPA purposes
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }



    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id) && type == role.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", type)
                .toString();
    }
}
