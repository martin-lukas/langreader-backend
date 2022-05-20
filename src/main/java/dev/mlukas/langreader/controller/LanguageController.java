package dev.mlukas.langreader.controller;

import dev.mlukas.langreader.model.User;
import dev.mlukas.langreader.repository.LangRepository;
import dev.mlukas.langreader.model.Language;
import dev.mlukas.langreader.repository.UserRepository;
import dev.mlukas.langreader.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/langs")
public class LanguageController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LangRepository langRepository;
    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Language>> getAllLangs() {
        List<Language> languages = langRepository.findAll();
        languages.sort(Comparator.comparing(Language::getFullName));
        return new ResponseEntity<>(languages, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Language>> getUserLangs() {
        Optional<User> user = userRepository.findByUsername(UserRepository.MARTIN);
        if (user.isPresent()) {
            List<Language> languages = user.get().getLangs();
            return new ResponseEntity<>(languages, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<String> addUserLang(@RequestBody Language newLang) {
        Optional<User> foundUser = userRepository.findByUsername(UserRepository.MARTIN);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            List<Language> usersLangs = user.getLangs();
            if (!usersLangs.contains(newLang)) {
                user.addLanguage(newLang);
                userRepository.save(user);
                return new ResponseEntity<>("New user language added.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("This user language is already added.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<String> removeUserLang(@RequestParam("id") int langId) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            User foundUser = userOpt.get();
            Language lang = langRepository.findById(langId);
            if (lang != null) {
                foundUser.removeLanguage(lang);
                wordRepository.deleteAllByUserAndLanguage(foundUser, lang);
                userRepository.save(foundUser);
                return new ResponseEntity<>("User language deleted.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(
                "Attempting to delete a user language not present in your database.",
                HttpStatus.BAD_REQUEST
        );
    }

    @GetMapping("/chosen")
    public ResponseEntity<Language> getChosenLang() {
        Optional<User> user = userRepository.findByUsername(UserRepository.MARTIN);
        if (user.isPresent()) {
            Language chosenlanguage = user.get().getChosenLang();
            return new ResponseEntity<>(chosenlanguage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/chosen")
    public ResponseEntity<String> updateChosenLang(@RequestBody Language newChosenLang) {
        Optional<User> user = userRepository.findByUsername(UserRepository.MARTIN);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setChosenLang(newChosenLang);
            userRepository.save(foundUser);
            return new ResponseEntity<>("Changed chosen lang.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Check if the language is valid.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/native")
    public ResponseEntity<Language> getNativeLang() {
        Optional<User> user = userRepository.findByUsername(UserRepository.MARTIN);
        if (user.isPresent()) {
            Language nativeLang = user.get().getNativeLang();
            return new ResponseEntity<>(nativeLang, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
