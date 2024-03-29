package dev.mlukas.langreader.statistics;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.security.User;
import dev.mlukas.langreader.security.UserService;
import dev.mlukas.langreader.text.WordService;
import dev.mlukas.langreader.text.WordType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatisticsController {
    private final UserService userService;
    private final WordService wordService;

    public StatisticsController(UserService userService, WordService wordService) {
        this.userService = userService;
        this.wordService = wordService;
    }

    @GetMapping
    @Transactional
    public List<LanguageStatistics> getStatistics(Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        List<Language> userLangs = foundUser.getLangs();

        return userLangs.stream()
                .map(userLang -> new LanguageStatistics(
                        userLang,
                        wordService.countByTypeAndLanguageAndUser(WordType.KNOWN, userLang, foundUser),
                        wordService.countByTypeAndLanguageAndUser(WordType.STUDIED, userLang, foundUser),
                        wordService.countByTypeAndLanguageAndUser(WordType.IGNORED, userLang, foundUser)
                )).sorted(Comparator.comparing(LanguageStatistics::knownCount).reversed())
                .toList();
    }
}
