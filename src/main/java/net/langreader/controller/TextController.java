package net.langreader.controller;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import net.langreader.repository.UserRepository;
import net.langreader.model.Language;
import net.langreader.model.User;
import net.langreader.model.Text;
import net.langreader.repository.TextRepository;
import net.langreader.model.UrlText;
import net.langreader.model.ParsedText;
import net.langreader.util.TextParser;
import net.langreader.model.Word;
import net.langreader.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/texts")
public class TextController {
    @Autowired
    private TextRepository textRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;

    @GetMapping
    public ResponseEntity<List<Text>> getTextTitles() {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
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
    public ResponseEntity<Text> getText(@PathVariable int textId) {
        Optional<User> user = userRepository.findByUsername(UserRepository.MARTIN);
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

    @GetMapping("/{textId}/parsed")
    public ResponseEntity<ParsedText> getParsedText(@PathVariable int textId) {
        Optional<User> user = userRepository.findByUsername(UserRepository.MARTIN);
        if (user.isPresent()) {
            Optional<Text> textOpt = textRepository.findById(textId);
            if (textOpt.isPresent()) {
                ParsedText parsedText = TextParser.parseText(textOpt.get());
                ParsedText enrichedText = enrichTextForUser(parsedText, user.get());
                return new ResponseEntity<>(enrichedText, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<String> addText(@RequestBody Text newText) {
        String textVal = newText.getText();
        if (textVal != null && !textVal.isEmpty()) {
            Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Language chosenLang = user.getChosenLang();
                if (chosenLang != null) {
                    newText.setId(null);
                    newText.setUser(user);
                    newText.setLanguage(chosenLang);
                    textRepository.save(newText);
                    return new ResponseEntity<>("New text added.", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(
                "Please check that the text data is valid (non-empty title and text).",
                HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<?> updateText(@RequestBody Text text) {
        if (isValid(text)) {
            Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
            if (userOpt.isPresent()) {
                Optional<Text> foundTextOpt = textRepository.findById(text.getId());
                if (foundTextOpt.isPresent()) {
                    Text foundText = foundTextOpt.get();
                    foundText.setTitle(text.getTitle());
                    foundText.setText(text.getText());
                    textRepository.save(foundText);
                    return new ResponseEntity<>("Text updated.", HttpStatus.OK);
                }
                return new ResponseEntity<>(
                        "Attempting to update a text not present in your database.",
                        HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Check if the text is valid.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteText(@RequestParam("id") int textId) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            if (textRepository.existsById(textId)) {
                textRepository.deleteById(textId);
                return new ResponseEntity<>("Text deleted.", HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    "Attempting to delete a text not present in your database.",
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/url")
    public ResponseEntity<String> addTextFromUrl(@RequestBody UrlText urlText) {
        try {
            InputSource is = new InputSource();
            is.setEncoding("UTF-8");
            is.setByteStream(new URL(urlText.getUrl()).openStream());
            String text = ArticleExtractor.INSTANCE.getText(is);
            return addText(new Text(urlText.getTitle(), text));
        } catch (IOException | BoilerpipeProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Couldn't add text from URL.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ParsedText enrichTextForUser(ParsedText parsedText, User user) {
        Language chosenLang = user.getChosenLang();

        parsedText.getParagraphs().forEach(paragraph -> paragraph.stream()
                .filter(token -> token.getType() != null)
                .forEach(token -> {
                    Optional<Word> foundWord = wordRepository.findByValueAndLanguageAndUser(
                            token.getValue().toLowerCase(), chosenLang, user);
                    foundWord.ifPresent(word -> token.setType(word.getType()));
                })
        );

        return parsedText;
    }

    private boolean isValid(Text text) {
        String title = text.getTitle();
        String textVal = text.getText();
        return title != null && !title.isBlank() && textVal != null && !textVal.isBlank();
    }
}
