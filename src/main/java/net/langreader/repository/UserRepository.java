package net.langreader.repository;

import net.langreader.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    String MARTIN = "martin";

    Optional<User> findByUsername(String username);
}
