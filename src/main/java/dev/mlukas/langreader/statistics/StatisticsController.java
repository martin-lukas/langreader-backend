package dev.mlukas.langreader.statistics;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.text.WordService;
import dev.mlukas.langreader.text.WordType;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
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
    public List<LanguageStatistics> getStatistics(Principal principal) {
        User foundUser = userService.getUser(principal.getName());

        List<LanguageStatistics> stats = new ArrayList<>();
        List<Language> userLangs = foundUser.getLangs();
        for (Language userLang : userLangs) {
            stats.add(new LanguageStatistics(
                    userLang,
                    wordService.countByTypeAndLanguageAndUser(WordType.KNOWN, userLang, foundUser),
                    wordService.countByTypeAndLanguageAndUser(WordType.STUDIED, userLang, foundUser),
                    wordService.countByTypeAndLanguageAndUser(WordType.IGNORED, userLang, foundUser)
            ));
        }
        stats.sort(Comparator.comparing(LanguageStatistics::knownCount).reversed());
        return stats;
    }
}
