package dev.mlukas.langreader.text;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.security.User;
import org.springframework.stereotype.Service;

@Service
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public Word getWordBy(String value, Language lang, User user) {
        return wordRepository.findByValueAndLanguageAndUser(value, lang, user)
                .orElseThrow(() ->
                        new WordNotFoundException(
                                "The word '%s' in language '%s' for the user '%s' doesn't exist.".formatted(value, lang, user.getUsername())
                        )
                );
    }

    public boolean existBy(String value, Language lang, User user) {
        return wordRepository.existsByValueAndLanguageAndUser(value, lang, user);
    }

    public int countByTypeAndLanguageAndUser(WordType type, Language language, User user) {
        return wordRepository.countByTypeAndLanguageAndUser(type, language, user);
    }

    public void save(Word word) {
        wordRepository.save(word);
    }

    public void delete(Word word) {
        wordRepository.delete(word);
    }

    public void deleteAllByUserAndLanguage(User user, Language language) {
        wordRepository.deleteAllByUserAndLanguage(user, language);
    }
}
