package net.langreader.repository;

import net.langreader.model.Language;
import net.langreader.model.User;
import net.langreader.model.Word;
import net.langreader.model.WordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByValueAndLanguageAndUser(String word, Language lang, User user);

    boolean existsByValueAndLanguageAndUser(String value, Language lang, User user);

    int countByTypeAndLanguageAndUser(WordType type, Language language, User user);

    void deleteAllByUserAndLanguage(User user, Language language);
}
