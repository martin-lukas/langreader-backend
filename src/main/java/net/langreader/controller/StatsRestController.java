package net.langreader.controller;

import net.langreader.dao.TextRepository;
import net.langreader.dao.UserRepository;
import net.langreader.dao.WordRepository;
import net.langreader.model.LangStatistics;
import net.langreader.model.Language;
import net.langreader.model.User;
import net.langreader.model.WordType;
import net.langreader.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stats")
public class StatsRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<LangStatistics>> getStatistics(HttpServletRequest req) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> userOpt = userRepository.findByUsername(username);
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
