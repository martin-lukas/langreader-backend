package dev.mlukas.langreader.text;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.InputSource;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/texts")
public class TextController {
    private final TextService textService;
    private final UserService userService;
    private final WordRepository wordRepository;

    public TextController(TextService textService, UserService userService, WordRepository wordRepository) {
        this.textService = textService;
        this.userService = userService;
        this.wordRepository = wordRepository;
    }

    @GetMapping
    public List<Text> getTextTitles() {
        User user = userService.getUser(UserService.MARTIN);
        Language chosenLang = user.getChosenLang();
        List<Text> texts = textService.getTextsBy(user, chosenLang);
        List<Text> strippedTexts = new ArrayList<>();
        for (Text text : texts) {
            text.setText(null);
            text.setUser(null);
            text.setLanguage(null);
            strippedTexts.add(text);
        }
        return strippedTexts;
    }

    @GetMapping("/{textId}")
    public Text getText(@PathVariable int textId) {
        // Check that the user exists, or throw an exception
        userService.getUser(UserService.MARTIN);

        Text foundText = textService.getText(textId);
        foundText.setLanguage(null);
        foundText.setUser(null);
        return foundText;
    }

    @GetMapping("/{textId}/parsed")
    public ParsedText getParsedText(@PathVariable int textId) {
        User foundUser = userService.getUser(UserService.MARTIN);
        Text foundText = textService.getText(textId);
        ParsedText parsedText = TextParser.parseText(foundText);
        return enrichTextForUser(parsedText, foundUser);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addText(@Valid @RequestBody TextCreateRequest textCreateRequest) {
        User foundUser = userService.getUser(UserService.MARTIN);
        Language chosenLang = foundUser.getChosenLang();
        textService.save(new Text(
                null, // To force new text creation
                textCreateRequest.title(),
                textCreateRequest.text(),
                chosenLang,
                foundUser
        ));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateText(@Valid @RequestBody TextUpdateRequest textUpdateRequest) {
        Text foundText = textService.getText(textUpdateRequest.id());
        foundText.setTitle(textUpdateRequest.title());
        foundText.setText(textUpdateRequest.text());
        textService.save(foundText);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteText(@RequestParam("id") int textId) {
        textService.delete(textId);
    }

    @PostMapping("/url")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTextFromUrl(@Valid @RequestBody TextFromUrlRequest textFromUrlRequest) {
        try {
            InputSource is = new InputSource();
            is.setEncoding("UTF-8");
            is.setByteStream(new URL(textFromUrlRequest.url()).openStream());
            String text = ArticleExtractor.INSTANCE.getText(is);
            addText(new TextCreateRequest(textFromUrlRequest.title(), text));
        } catch (IOException | BoilerpipeProcessingException e) {
            throw new TextFromUrlProcessingException(e);
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
}
