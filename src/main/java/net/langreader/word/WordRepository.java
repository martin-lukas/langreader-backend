package net.langreader.word;

import net.langreader.language.Language;
import net.langreader.security.User;
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
