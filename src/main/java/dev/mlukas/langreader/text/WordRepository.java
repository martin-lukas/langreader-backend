package dev.mlukas.langreader.text;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByValueAndLanguageAndUser(String word, Language lang, User user);

    boolean existsByValueAndLanguageAndUser(String value, Language lang, User user);

    int countByTypeAndLanguageAndUser(WordType type, Language language, User user);

    void deleteAllByUserAndLanguage(User user, Language language);
}
