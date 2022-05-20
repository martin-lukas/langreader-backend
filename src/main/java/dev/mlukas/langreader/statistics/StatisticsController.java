package dev.mlukas.langreader.statistics;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.text.WordRepository;
import dev.mlukas.langreader.text.WordType;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stats")
public class StatisticsController {
    private final UserRepository userRepository;
    private final WordRepository wordRepository;

    public StatisticsController(UserRepository userRepository, WordRepository wordRepository) {
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
    }

    @GetMapping
    public ResponseEntity<List<LanguageStatistics>> getStatistics() {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<LanguageStatistics> stats = new ArrayList<>();
            List<Language> userLangs = user.getLangs();
            for (Language userLang : userLangs) {
                stats.add(new LanguageStatistics(
                        userLang,
                        wordRepository.countByTypeAndLanguageAndUser(WordType.KNOWN, userLang, user),
                        wordRepository.countByTypeAndLanguageAndUser(WordType.STUDIED, userLang, user),
                        wordRepository.countByTypeAndLanguageAndUser(WordType.IGNORED, userLang, user)
                ));
            }
            stats.sort(Comparator.comparing(LanguageStatistics::getKnownCount).reversed());
            return new ResponseEntity<>(stats, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
