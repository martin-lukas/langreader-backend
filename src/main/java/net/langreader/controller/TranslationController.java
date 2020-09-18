package net.langreader.controller;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import net.langreader.repository.LangRepository;
import net.langreader.repository.UserRepository;
import net.langreader.model.Language;
import net.langreader.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {
    @Value("${langreader.app.googleApiKey}")
    private String googleApiKey;

    @Autowired
    private LangRepository langRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> getTranslation(@RequestParam(value = "word") String word) {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            Language chosenLang = userOpt.get().getChosenLang();
            if (chosenLang != null) {
                @SuppressWarnings("deprecation") Translate translate = TranslateOptions.newBuilder()
                        .setApiKey(googleApiKey).build().getService();
                // for now, default is EN, and if chosen is EN, then translated to user's native
                Language englishLang = langRepository.findByCode("EN");
                Language targetLang = (chosenLang.getCode().equals("EN"))
                        ? userOpt.get().getNativeLang()
                        : englishLang;

                Translation translation = translate.translate(
                        word,
                        Translate.TranslateOption.sourceLanguage(chosenLang.getCode()),
                        Translate.TranslateOption.targetLanguage(targetLang.getCode()));
                return new ResponseEntity<>(translation.getTranslatedText(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
