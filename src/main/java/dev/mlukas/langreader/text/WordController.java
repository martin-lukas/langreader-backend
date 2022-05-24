package dev.mlukas.langreader.text;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
    public void addWord(@Valid @RequestBody TokenUpdateRequest token) {
        User foundUser = userService.getUser(UserService.MARTIN);
        Language chosenLang = foundUser.getChosenLang();
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
    public void updateWord(@Valid @RequestBody TokenUpdateRequest tokenRequest) {
        User foundUser = userService.getUser(UserService.MARTIN);

        String value = tokenRequest.value().toLowerCase();
        WordType newType = tokenRequest.type();
        if (tokenRequest.type() != WordType.UNKNOWN) { // Instead of setting unknown type, just delete the word
            Word foundWord = wordService.getWordBy(value, foundUser.getChosenLang(), foundUser);
            if (foundWord.getType() != newType) {
                foundWord.setType(newType);
                wordService.save(foundWord);
            }
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWord(@NotBlank @RequestParam("word") String value) {
        User foundUser = userService.getUser(UserService.MARTIN);
        Word foundWord = wordService.getWordBy(value.toLowerCase(), foundUser.getChosenLang(), foundUser);
        wordService.delete(foundWord);
    }
}
