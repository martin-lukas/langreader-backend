package dev.mlukas.langreader.language;

import dev.mlukas.langreader.ErrorMessage;
import dev.mlukas.langreader.text.TextService;
import dev.mlukas.langreader.text.WordService;
import dev.mlukas.langreader.security.User;
import dev.mlukas.langreader.security.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
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
    private final TextService textService;
    private final WordService wordService;

    public LanguageController(UserService userService, LanguageService languageService, TextService textService, WordService wordService) {
        this.userService = userService;
        this.languageService = languageService;
        this.textService = textService;
        this.wordService = wordService;
    }

    @GetMapping("/all")
    public List<Language> getAllLangs() {
        List<Language> languages = languageService.getAllLanguages();
        languages.sort(Comparator.comparing(Language::getFullName));
        return languages;
    }

    @GetMapping
    @Transactional
    public List<Language> getUserLangs(Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        return foundUser.getLangs();
    }

    @PutMapping("/chosen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateChosenLang(@Valid @RequestBody LanguageChangeRequest newChosenLang, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Language foundLanguage = languageService.getLanguageByCodeAndFullName(newChosenLang.code(), newChosenLang.fullName());

        foundUser.setChosenLang(foundLanguage);

        userService.save(foundUser);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
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
    @Transactional
    public void removeUserLang(@RequestParam("id") int langId, Principal principal) {
        User foundUser = userService.getUser(principal.getName());

        Language userLang = languageService.getLanguage(langId);
        foundUser.removeLanguage(userLang);
        textService.deleteAllByUserAndLanguage(foundUser, userLang);
        wordService.deleteAllByUserAndLanguage(foundUser, userLang);
        userService.save(foundUser);
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
