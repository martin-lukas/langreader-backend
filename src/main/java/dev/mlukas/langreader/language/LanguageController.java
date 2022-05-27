package dev.mlukas.langreader.language;

import dev.mlukas.langreader.ErrorMessage;
import dev.mlukas.langreader.text.WordService;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
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
    public List<Language> getUserLangs(Principal principal) {
        User user = userService.getUser(principal.getName());
        return user.getLangs();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addUserLang(@Valid @RequestBody LanguageChangeRequest newLang, Principal principal) {
        User foundUser = userService.getUser(principal.getName());

        Language foundLanguage = languageService.getLanguageByCodeAndFullName(newLang.code(), newLang.fullName());
        List<Language> usersLangs = foundUser.getLangs();
        if (usersLangs.contains(foundLanguage)) {
            throw new UserLanguageAlreadyExistsException(foundUser.getUsername(), foundLanguage.getFullName());
        }

        foundUser.addLanguage(foundLanguage);
        userService.save(foundUser);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserLang(@RequestParam("id") int langId, Principal principal) {
        User foundUser = userService.getUser(principal.getName());

        Language userLang = languageService.getLanguage(langId);
        foundUser.removeLanguage(userLang);
        wordService.deleteAllByUserAndLanguage(foundUser, userLang);
        userService.save(foundUser);
    }

    @GetMapping("/chosen")
    public Language getChosenLang(Principal principal) {
        User foundUser = userService.getUser(principal.getName());

        @Nullable Language chosenLang = foundUser.getChosenLang();
        if (chosenLang == null) {
            throw new NoChosenLanguageException(foundUser.getUsername());
        }

        return chosenLang;
    }

    @PutMapping("/chosen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChosenLang(@Valid @RequestBody LanguageChangeRequest newChosenLang, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Language foundLanguage = languageService.getLanguageByCodeAndFullName(newChosenLang.code(), newChosenLang.fullName());

        foundUser.setChosenLang(foundLanguage);
        userService.save(foundUser);
    }

    @GetMapping("/native")
    public Language getNativeLang(Principal principal) {
        User user = userService.getUser(principal.getName());
        return user.getNativeLang();
    }

    @ExceptionHandler(UserLanguageAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage userLanguageAreadyExists(UserLanguageAlreadyExistsException exception) {
        return new ErrorMessage(HttpStatus.CONFLICT, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(LanguageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage languageNotFound(LanguageNotFoundException exception) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());
    }
}
