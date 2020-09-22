package net.langreader.word;

import net.langreader.security.UserRepository;
import net.langreader.language.Language;
import net.langreader.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/words")
public class WordController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;

    @PostMapping("/enrich")
    public ResponseEntity<List<Word>> enrichWords(
            HttpServletRequest req, @RequestBody List<Word> words) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Word> enrichedWords = new ArrayList<>();
            for (Word typelessWord : words) {
                Optional<Word> foundWord = wordRepository.findByWordAndLanguageAndUser(
                        typelessWord.getWord().toLowerCase(), user.getChosenLang(), user);
                if (foundWord.isPresent()) {
                    typelessWord.setType(foundWord.get().getType());
                    enrichedWords.add(typelessWord);
                }
            }
            return new ResponseEntity<>(enrichedWords, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> addWord(HttpServletRequest req, @RequestBody Word newWord) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            String newWordVal = newWord.getWord();
            if (newWordVal != null && !newWordVal.isEmpty() && newWord.getType() != null) {
                User user = userOpt.get();
                Language chosenLang = user.getChosenLang();
                if (!wordRepository.existsByWordAndLanguageAndUser(newWordVal, chosenLang, user)) {
                    newWord.setId(null);
                    newWord.setLanguage(user.getChosenLang());
                    newWord.setUser(user);
                    wordRepository.save(newWord);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<?> updateWord(HttpServletRequest req, @RequestBody Word word) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            String wordVal = word.getWord();
            if (wordVal != null && !wordVal.isEmpty() && word.getType() != null) {
                User user = userOpt.get();
                Optional<Word> foundWordOpt = wordRepository.findByWordAndLanguageAndUser(
                        word.getWord(), user.getChosenLang(), user);
                if (foundWordOpt.isPresent()) {
                    Word foundWord = foundWordOpt.get();
                    foundWord.setType(word.getType());
                    wordRepository.save(foundWord);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteWord(HttpServletRequest req, @RequestBody Word word) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            String wordVal = word.getWord();
            if (wordVal != null && !wordVal.isEmpty()) {
                User user = userOpt.get();
                Optional<Word> foundWordOpt = wordRepository.findByWordAndLanguageAndUser(
                        word.getWord(), user.getChosenLang(), user);
                if (foundWordOpt.isPresent()) {
                    wordRepository.delete(foundWordOpt.get());
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}