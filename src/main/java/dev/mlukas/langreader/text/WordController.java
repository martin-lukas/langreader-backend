package dev.mlukas.langreader.text;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/words")
public class WordController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;

    @PostMapping
    public ResponseEntity<String> addWord(@RequestBody Token token) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            String value = token.getValue();
            if (token.isValid(true)) {
                User user = userOpt.get();
                Language chosenLang = user.getChosenLang();
                if (!wordRepository.existsByValueAndLanguageAndUser(value, chosenLang, user)) {
                    Word newWord = new Word(null, token.getValue().toLowerCase(), token.getType(), chosenLang, user);
                    wordRepository.save(newWord);
                    return new ResponseEntity<>("New word added.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(
                            "This word is already present in your database. " +
                                    "For updating its type, use PUT method.",
                            HttpStatus.BAD_REQUEST
                    );
                }
            }
        }

        return new ResponseEntity<>(
                "Please check that the token data is valid (non-empty value and type not null nor UNKNOWN).",
                HttpStatus.BAD_REQUEST
        );
    }

    @PutMapping
    public ResponseEntity<String> updateWord(@RequestBody Token token) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            String value = token.getValue().toLowerCase();
            WordType newType = token.getType();
            if (token.isValid(true)) {
                User user = userOpt.get();
                Optional<Word> foundWordOpt = wordRepository.findByValueAndLanguageAndUser(
                        value,
                        user.getChosenLang(),
                        user
                );
                if (foundWordOpt.isPresent()) {
                    Word foundWord = foundWordOpt.get();
                    if (foundWord.getType() != newType) {
                        foundWord.setType(token.getType());
                        wordRepository.save(foundWord);
                        return new ResponseEntity<>("Word type updated.", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(
                                "The provided type doesn't differ from the old one.",
                                HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>(
                            "The provided word doesn't exist yet. For adding it, use POST method.",
                            HttpStatus.BAD_REQUEST
                    );
                }
            }
        }

        return new ResponseEntity<>(
                "Please check that the token data is valid " +
                    "(existing value and type not null nor UNKNOWN).",
                HttpStatus.BAD_REQUEST
        );
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWord(@RequestParam("word") String value) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            if (value != null && !value.isBlank()) {
                User user = userOpt.get();
                Optional<Word> foundWordOpt = wordRepository.findByValueAndLanguageAndUser(
                        value.toLowerCase(),
                        user.getChosenLang(),
                        user
                );
                if (foundWordOpt.isPresent()) {
                    wordRepository.delete(foundWordOpt.get());
                    return new ResponseEntity<>("Word deleted.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(
                            "Attempting to delete a word not present in your database.",
                            HttpStatus.BAD_REQUEST
                    );
                }
            }
        }

        return new ResponseEntity<>(
                "Please check that the token data is valid (existing value)",
                HttpStatus.BAD_REQUEST
        );
    }
}
