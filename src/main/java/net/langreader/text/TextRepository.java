package net.langreader.text;

import net.langreader.language.Language;
import net.langreader.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Integer> {
    List<Text> findAllByUserAndLanguageOrderByIdDesc(User user, Language language);
}
