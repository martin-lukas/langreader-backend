package dev.mlukas.langreader.text;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import dev.mlukas.langreader.ErrorMessage;
import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.security.User;
import dev.mlukas.langreader.security.UserService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.InputSource;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.time.LocalDateTime;
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
    public List<StrippedText> getTextTitles(Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Language chosenLang = foundUser.getChosenLang();
        List<Text> texts = foundUser.getTexts();
        return texts.stream()
                .filter(text -> text.getLanguage().equals(chosenLang))
                .map(text -> new StrippedText(text.getId(), text.getTitle()))
                .toList();
    }

    @GetMapping("/{textId}")
    public FullText getText(@PathVariable int textId, Principal principal) {
        Text foundText = getPermittedTextOrThrow(textId, userService.getUser(principal.getName()));
        return new FullText(foundText);
    }

    @GetMapping("/{textId}/parsed")
    @Transactional
    public ParsedText getParsedText(@PathVariable int textId, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Text foundText = getPermittedTextOrThrow(textId, foundUser);
        Language chosenLang = foundUser.getChosenLang();

        ParsedText parsedText = TextParser.parseText(foundText);

        // Enrich tokens in the text with user's marked types.
        parsedText.getParagraphs().forEach(
                paragraph -> paragraph.stream()
                        .filter(token -> token.getType() != null)
                        .forEach(token -> {
                            @Nullable WordType foundWordType = wordService.getWordTypeBy(
                                    token.getValue().toLowerCase(),
                                    chosenLang,
                                    foundUser
                            );
                            token.setType(foundWordType);
                        })
        );

        return parsedText;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void addText(@Valid @RequestBody TextCreateRequest textCreateRequest, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
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
    @Transactional
    public void updateText(@Valid @RequestBody TextUpdateRequest textUpdateRequest, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Text foundText = getPermittedTextOrThrow(textUpdateRequest.id(), foundUser);

        foundText.setTitle(textUpdateRequest.title());
        foundText.setText(textUpdateRequest.text());

        textService.save(foundText);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteText(@RequestParam("id") int textId, Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        Text foundText = getPermittedTextOrThrow(textId, foundUser);

        textService.delete(foundText);
    }

    @PostMapping("/url")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
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
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                "The text with the given ID was not found.",
                LocalDateTime.now()
        );
    }

    private Text getPermittedTextOrThrow(int textId, User user) {
        Text foundText = textService.getText(textId);
        if (user.getId() != foundText.getUser().getId()) {
            throw new AccessDeniedException("text with ID '%d'".formatted(textId));
        }
        return foundText;
    }
}
