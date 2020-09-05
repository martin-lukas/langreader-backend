package net.langreader.controller;

import net.langreader.dao.TextRepository;
import net.langreader.dao.UserRepository;
import net.langreader.model.Language;
import net.langreader.model.Text;
import net.langreader.model.User;
import net.langreader.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/texts")
public class TextRestController {
    @Autowired
    private TextRepository textRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<Text>> getTextTitles(HttpServletRequest req) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Language chosenLang = user.getChosenLang();
            if (chosenLang != null) {
                List<Text> texts = textRepository.findAllByUserAndLanguageOrderByIdDesc(
                        user, chosenLang);
                List<Text> strippedTexts = new ArrayList<>();
                for (Text text : texts) {
                    text.setText(null);
                    text.setUser(null);
                    text.setLanguage(null);
                    strippedTexts.add(text);
                }
                return new ResponseEntity<>(strippedTexts, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{textId}")
    public ResponseEntity<Text> getText(HttpServletRequest req, @PathVariable int textId) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Optional<Text> textOpt = textRepository.findById(textId);
            if (textOpt.isPresent()) {
                Text text = textOpt.get();
                text.setLanguage(null);
                text.setUser(null);
                return new ResponseEntity<>(text, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> addText(HttpServletRequest req, @RequestBody Text newText) {
        String textVal = newText.getText();
        if (textVal != null && !textVal.isEmpty()) {
            String username = jwtUtils.getUsernameFromHttpRequest(req);
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Language chosenLang = user.getChosenLang();
                if (chosenLang != null) {
                    newText.setId(null);
                    newText.setUser(user);
                    newText.setLanguage(chosenLang);
                    textRepository.save(newText);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<?> updateText(HttpServletRequest req, @RequestBody Text text) {
        String textVal = text.getText();
        if (textVal != null && !textVal.isEmpty()) {
            String username = jwtUtils.getUsernameFromHttpRequest(req);
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                Optional<Text> foundTextOpt = textRepository.findById(text.getId());
                if (foundTextOpt.isPresent()) {
                    Text foundText = foundTextOpt.get();
                    foundText.setTitle(text.getTitle());
                    foundText.setText(text.getText());
                    textRepository.save(foundText);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteText(HttpServletRequest req, @RequestBody Text text) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            if (textRepository.existsById(text.getId())) {
                textRepository.deleteById(text.getId());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/batch")
    public ResponseEntity<?> addTexts(HttpServletRequest req, @RequestBody List<Text> newTexts) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Language chosenLang = user.getChosenLang();
            if (chosenLang != null) {
                boolean isTextListValid = true;
                for (Text newText : newTexts) {
                    String textVal = newText.getText();
                    if (textVal == null || textVal.isEmpty()) {
                        isTextListValid = false;
                        break;
                    }
                }
                if (isTextListValid) {
                    for (Text newText : newTexts) {
                        newText.setId(null);
                        newText.setUser(user);
                        newText.setLanguage(chosenLang);
                        textRepository.save(newText);
                    }
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
