package net.langreader.word;

import net.langreader.security.UserRepository;
import net.langreader.language.Language;
import net.langreader.security.User;
import net.langreader.text.parsing.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/enrich")
//    public ResponseEntity<List<Word>> enrichWords(@RequestBody List<Word> words) {
//        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            List<Word> enrichedWords = new ArrayList<>();
//            for (Word typelessWord : words) {
//                Optional<Word> foundWord = wordRepository.findByWordAndLanguageAndUser(
//                        typelessWord.getWord().toLowerCase(), user.getChosenLang(), user);
//                if (foundWord.isPresent()) {
//                    typelessWord.setType(foundWord.get().getType());
//                    enrichedWords.add(typelessWord);
//                }
//            }
//            return new ResponseEntity<>(enrichedWords, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

    @PostMapping
    public ResponseEntity<String> addWord(@RequestBody Token token) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            String value = token.getValue();
            if (token.isValid(true)) {
                User user = userOpt.get();
                Language chosenLang = user.getChosenLang();
                if (!wordRepository.existsByValueAndLanguageAndUser(value, chosenLang, user)) {
                    Word newWord = new Word(null, token.getValue(), token.getType(), chosenLang, user);
                    wordRepository.save(newWord);
                    return new ResponseEntity<>("New word successfully added.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(
                            "This word is already present in your database. " +
                                    "For updating its type, use PUT method.",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(
                "Please check that the token data is valid " +
                        "(non-empty value and type not null nor UNKNOWN).",
                HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<String> updateWord(@RequestBody Token token) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            String value = token.getValue();
            WordType newType = token.getType();
            if (token.isValid(true)) {
                User user = userOpt.get();
                Optional<Word> foundWordOpt = wordRepository.findByValueAndLanguageAndUser(
                        value, user.getChosenLang(), user);
                if (foundWordOpt.isPresent()) {
                    Word foundWord = foundWordOpt.get();
                    if (foundWord.getType() != newType) {
                        foundWord.setType(token.getType());
                        wordRepository.save(foundWord);
                        return new ResponseEntity<>("Word type successfully updated.", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(
                                "The provided type doesn't differ from the old one.",
                                HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>(
                            "The provided word doesn't exist yet. " +
                                "For adding it, use POST method.",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(
                "Please check that the token data is valid " +
                    "(existing value and type not null nor UNKNOWN).",
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWord(@RequestBody Token token) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            if (token.isValid(false)) {
                User user = userOpt.get();
                Optional<Word> foundWordOpt = wordRepository.findByValueAndLanguageAndUser(
                        token.getValue(), user.getChosenLang(), user);
                if (foundWordOpt.isPresent()) {
                    wordRepository.delete(foundWordOpt.get());
                    return new ResponseEntity<>("Word successfully deleted.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(
                            "Attempting to delete a word not present in your database.",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(
                "Please check that the token data is valid (existing value)",
                HttpStatus.BAD_REQUEST);
    }
}
