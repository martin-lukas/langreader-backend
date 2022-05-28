package dev.mlukas.langreader.text;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.security.User;
import dev.mlukas.langreader.security.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;

@RestController
@RequestMapping("/words")
public class WordController {
    private final UserService userService;
    private final WordService wordService;

    public WordController(UserService userService, WordService wordService) {
        this.userService = userService;
        this.wordService = wordService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addWord(@Valid @RequestBody TokenUpdateRequest token, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Language chosenLang = foundUser.getChosenLangOrThrow();

        String value = token.value();
        if (wordService.existBy(value, chosenLang, foundUser)) {
            throw new DuplicateWordException(
                    "The word '%s' is already present in your database. For updating its type, use PUT method.".formatted(value)
            );
        }

        Word newWord = new Word(null, value.toLowerCase(), token.type(), chosenLang, foundUser);
        wordService.save(newWord);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateWord(@Valid @RequestBody TokenUpdateRequest tokenRequest, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Language chosenLang = foundUser.getChosenLangOrThrow();

        String value = tokenRequest.value().toLowerCase();
        WordType newType = tokenRequest.type();
        if (tokenRequest.type() != WordType.UNKNOWN) { // Instead of setting unknown type, just delete the word
            Word foundWord = wordService.getWordBy(value, chosenLang, foundUser);
            if (foundWord.getType() != newType) {
                foundWord.setType(newType);
                wordService.save(foundWord);
            }
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteWord(@NotBlank @RequestParam("word") String value, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Language chosenLang = foundUser.getChosenLangOrThrow();
        Word foundWord = wordService.getWordBy(value.toLowerCase(), chosenLang, foundUser);

        wordService.delete(foundWord);
    }
}
