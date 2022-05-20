package dev.mlukas.langreader.repository;

import dev.mlukas.langreader.exception.UserNotFoundException;
import dev.mlukas.langreader.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    String MARTIN = "martin";

    Optional<User> findByUsername(String username);

    default User getUserByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("The user with the username '%s' doesn't exist.".formatted(username))
                );
    }
}
