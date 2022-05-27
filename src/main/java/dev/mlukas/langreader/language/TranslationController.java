package dev.mlukas.langreader.language;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/translate")
public class TranslationController {
    private final UserService userService;
    private final String googleApiKey;

    public TranslationController(
            UserService userService,
            @Value("${langreader.app.googleApiKey}") String googleApiKey
    ) {
        this.userService = userService;
        this.googleApiKey = googleApiKey;
    }

    @GetMapping
    @Transactional
    public String getTranslation(@RequestParam(value = "word") String word, Principal principal) {
        User foundUser = userService.getUser(principal.getName());

        @Nullable Language chosenLang = foundUser.getChosenLang();
        if (chosenLang == null) {
            throw new NoChosenLanguageException(foundUser.getUsername());
        }

        @Nullable Language nativeLang = foundUser.getNativeLang();
        // For now, default is EN, and if chosen is EN, then translated to user's native
        Language targetLang = (chosenLang != Language.DEFAULT_LANGUAGE) ? Language.DEFAULT_LANGUAGE : nativeLang;

        Translate translate = TranslateOptions.newBuilder().setApiKey(googleApiKey).build().getService();
        Translation translation = translate.translate(
                word,
                Translate.TranslateOption.sourceLanguage(chosenLang.getCode()),
                Translate.TranslateOption.targetLanguage(targetLang.getCode())
        );
        
        return translation.getTranslatedText().toLowerCase();
    }
}
