package dev.mlukas.langreader.controller;

import dev.mlukas.langreader.language.LangStatistics;
import dev.mlukas.langreader.model.Language;
import dev.mlukas.langreader.model.User;
import dev.mlukas.langreader.model.WordType;
import dev.mlukas.langreader.repository.UserRepository;
import dev.mlukas.langreader.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StatsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;

    @GetMapping
    public ResponseEntity<List<LangStatistics>> getStatistics() {
        Optional<User> userOpt = userRepository.findByUsername(UserRepository.MARTIN);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<LangStatistics> stats = new ArrayList<>();
            List<Language> userLangs = user.getLangs();
            for (Language userLang : userLangs) {
                stats.add(new LangStatistics(
                        userLang,
                        wordRepository.countByTypeAndLanguageAndUser(WordType.KNOWN, userLang, user),
                        wordRepository.countByTypeAndLanguageAndUser(WordType.STUDIED, userLang, user),
                        wordRepository.countByTypeAndLanguageAndUser(WordType.IGNORED, userLang, user)
                ));
            }
            stats.sort(Comparator.comparing(LangStatistics::getKnownCount).reversed());
            return new ResponseEntity<>(stats, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
