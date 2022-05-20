package dev.mlukas.langreader.repository;

import dev.mlukas.langreader.model.Language;
import dev.mlukas.langreader.model.User;
import dev.mlukas.langreader.model.Word;
import dev.mlukas.langreader.model.WordType;
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
