package dev.mlukas.langreader.language;

import dev.mlukas.langreader.text.WordService;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/langs")
public class LanguageController {
    private final UserService userService;
    private final LanguageService languageService;
    private final WordService wordService;

    public LanguageController(UserService userService, LanguageService languageService, WordService wordService) {
        this.userService = userService;
        this.languageService = languageService;
        this.wordService = wordService;
    }

    @GetMapping("/all")
    public List<Language> getAllLangs() {
        List<Language> languages = languageService.getAllLanguages();
        languages.sort(Comparator.comparing(Language::getFullName));
        return languages;
    }

    @GetMapping
    public List<Language> getUserLangs() {
        User user = userService.getUser(UserService.MARTIN);
        return user.getLangs();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addUserLang(@Valid @RequestBody LanguageChangeRequest newLang) {
        User foundUser = userService.getUser(UserService.MARTIN);
        Language foundLanguage = languageService.getLanguageByCodeAndFullName(newLang.code(), newLang.fullName());
        List<Language> usersLangs = foundUser.getLangs();
        if (!usersLangs.contains(foundLanguage)) {
            foundUser.addLanguage(foundLanguage);
            userService.save(foundUser);
            return new ResponseEntity<>("New user language added.", HttpStatus.OK);
        }
        return new ResponseEntity<>("This user language is already added.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserLang(@RequestParam("id") int langId) {
        User foundUser = userService.getUser(UserService.MARTIN);
        Language lang = languageService.getLanguage(langId);
        foundUser.removeLanguage(lang);
        wordService.deleteAllByUserAndLanguage(foundUser, lang);
        userService.save(foundUser);
    }

    @GetMapping("/chosen")
    public @Nullable Language getChosenLang() {
        User user = userService.getUser(UserService.MARTIN);
        // TODO: refactor to return exception instead of null
        return user.getChosenLang();
    }

    @PutMapping("/chosen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChosenLang(@Valid @RequestBody LanguageChangeRequest newChosenLang) {
        User foundUser = userService.getUser(UserService.MARTIN);
        Language foundLanguage = languageService.getLanguageByCodeAndFullName(newChosenLang.code(), newChosenLang.fullName());
        foundUser.setChosenLang(foundLanguage);
        userService.save(foundUser);
    }

    @GetMapping("/native")
    public @Nullable Language getNativeLang() {
        User user = userService.getUser(UserService.MARTIN);
        // TODO: refactor to return exception instead of null
        return user.getNativeLang();
    }
}
