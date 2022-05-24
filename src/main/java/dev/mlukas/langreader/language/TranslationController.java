package dev.mlukas.langreader.language;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/translate")
public class TranslationController {
    private final LanguageService languageService;
    private final UserService userService;
    private final String googleApiKey;

    public TranslationController(
            LanguageService languageService,
            UserService userService,
            @Value("${langreader.app.googleApiKey}") String googleApiKey
    ) {
        this.languageService = languageService;
        this.userService = userService;
        this.googleApiKey = googleApiKey;
    }

    @GetMapping
    public ResponseEntity<String> getTranslation(@RequestParam(value = "word") String word) {
        User foundUser = userService.getUser(UserService.MARTIN);
        Language chosenLang = foundUser.getChosenLang();
        // For now, default is EN, and if chosen is EN, then translated to user's native
        Language englishLang = languageService.getLanguage("EN");
        @Nullable Language nativeLang = foundUser.getNativeLang();
        Language targetLang = (chosenLang.getCode().equals("EN")) && nativeLang != null
                ? nativeLang
                : englishLang;

        Translate translate = TranslateOptions.newBuilder().setApiKey(googleApiKey).build().getService();
        Translation translation = translate.translate(
                word,
                Translate.TranslateOption.sourceLanguage(chosenLang.getCode()),
                Translate.TranslateOption.targetLanguage(targetLang.getCode())
        );
        
        return new ResponseEntity<>(translation.getTranslatedText().toLowerCase(), HttpStatus.OK);
    }
}
