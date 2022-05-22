package dev.mlukas.langreader.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByType(RoleType type);
}
