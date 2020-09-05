package net.langreader.controller;

import net.langreader.dao.LangRepository;
import net.langreader.dao.UserRepository;
import net.langreader.dao.WordRepository;
import net.langreader.model.Language;
import net.langreader.model.User;
import net.langreader.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/langs")
public class LanguageRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LangRepository langRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/all")
    public ResponseEntity<List<Language>> getAllLangs() {
        List<Language> languages = langRepository.findAll();
        languages.sort(Comparator.comparing(Language::getFullName));
        return new ResponseEntity<>(languages, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Language>> getUserLangs(HttpServletRequest req) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            List<Language> languages = user.get().getLangs();
            return new ResponseEntity<>(languages, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> addUserLang(HttpServletRequest req, @RequestBody Language newLang) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            List<Language> usersLangs = user.getLangs();
            if (!usersLangs.contains(newLang)) {
                user.addLanguage(newLang);
                userRepository.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> removeUserLang(HttpServletRequest req, @RequestBody Language language) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.removeLanguage(language);
            wordRepository.deleteAllByUserAndLanguage(foundUser, language);
            userRepository.save(foundUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/chosen")
    public ResponseEntity<Language> getChosenLang(HttpServletRequest req) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Language chosenlanguage = user.get().getChosenLang();
            return new ResponseEntity<>(chosenlanguage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/chosen")
    public ResponseEntity<?> updateChosenLang(
            HttpServletRequest req, @RequestBody Language newChosenLang) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setChosenLang(newChosenLang);
            userRepository.save(foundUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/native")
    public ResponseEntity<Language> getNativeLang(HttpServletRequest req) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Language nativeLang = user.get().getNativeLang();
            return new ResponseEntity<>(nativeLang, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
