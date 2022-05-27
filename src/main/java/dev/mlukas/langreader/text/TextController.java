package dev.mlukas.langreader.text;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import dev.mlukas.langreader.ErrorMessage;
import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.language.NoChosenLanguageException;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.InputSource;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/texts")
public class TextController {
    private final TextService textService;
    private final UserService userService;
    private final WordService wordService;

    public TextController(TextService textService, UserService userService, WordService wordService) {
        this.textService = textService;
        this.userService = userService;
        this.wordService = wordService;
    }

    @GetMapping
    @Transactional
    public List<Text> getTextTitles(Principal principal) {
        User foundUser = userService.getUser(principal.getName());

        @Nullable Language chosenLang = foundUser.getChosenLang();
        if (chosenLang == null) {
            throw new NoChosenLanguageException(foundUser.getUsername());
        }

        List<Text> texts = textService.getTextsBy(foundUser, chosenLang);
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
        // TODO: Check that the user fetching the text owns it.
        Text foundText = textService.getText(textId);
        foundText.setLanguage(null);
        foundText.setUser(null);
        return foundText;
    }

    @GetMapping("/{textId}/parsed")
    @Transactional
    public ParsedText getParsedText(@PathVariable int textId, Principal principal) {
        // TODO: Check that the user fetching the text owns it.
        User foundUser = userService.getUser(principal.getName());

        if (foundUser.getChosenLang() == null) {
            throw new NoChosenLanguageException(foundUser.getUsername());
        }

        Language chosenLang = foundUser.getChosenLang();
        Text foundText = textService.getText(textId);
        ParsedText parsedText = TextParser.parseText(foundText);

        // Enrich tokens in the text with user's marked types.
        parsedText.getParagraphs().forEach(paragraph -> paragraph.stream()
                .filter(token -> token.getType() != null)
                .forEach(token -> {
                    Word foundWord = wordService.getWordBy(token.getValue().toLowerCase(), chosenLang, foundUser);
                    token.setType(foundWord.getType());
                })
        );

        return parsedText;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addText(@Valid @RequestBody TextCreateRequest textCreateRequest, Principal principal) {
        User foundUser = userService.getUser(principal.getName());

        @Nullable Language chosenLang = foundUser.getChosenLang();
        if (chosenLang == null) {
            throw new NoChosenLanguageException(foundUser.getUsername());
        }

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
    @Transactional
    public void updateText(@Valid @RequestBody TextUpdateRequest textUpdateRequest) {
        // TODO: Check that the user updating the text owns it.
        Text foundText = textService.getText(textUpdateRequest.id());
        foundText.setTitle(textUpdateRequest.title());
        foundText.setText(textUpdateRequest.text());
        textService.save(foundText);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteText(@RequestParam("id") int textId) {
        // TODO: Check that the user deleting the text owns it.
        textService.delete(textId);
    }

    @PostMapping("/url")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTextFromUrl(@Valid @RequestBody TextFromUrlRequest textFromUrlRequest, Principal principal) {
        try {
            InputSource is = new InputSource();
            is.setEncoding("UTF-8");
            is.setByteStream(new URL(textFromUrlRequest.url()).openStream());
            String text = ArticleExtractor.INSTANCE.getText(is);
            addText(new TextCreateRequest(textFromUrlRequest.title(), text), principal);
        } catch (IOException | BoilerpipeProcessingException e) {
            throw new TextFromUrlProcessingException(e);
        }
    }

    @ExceptionHandler(TextNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage textNotFound(TextNotFoundException exception) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST, "The text with the given ID was not found.", LocalDateTime.now());
    }
}
