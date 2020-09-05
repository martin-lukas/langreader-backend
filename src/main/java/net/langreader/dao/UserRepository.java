package net.langreader.dao;

import net.langreader.model.Language;
import net.langreader.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    List<User> findAllByOrderByUsername();

    boolean existsByUsername(String username);
}
