package dev.mlukas.langreader.text;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextService {
    private final TextRepository textRepository;

    public TextService(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    public Text getText(int id) {
        return textRepository.findById(id)
                .orElseThrow(() ->
                        new TextNotFoundException("The text with the ID '%d' doesn't exist.".formatted(id))
                );
    }

    public List<Text> getTextsBy(User user, Language language) {
        return textRepository.findAllByUserAndLanguageOrderByIdDesc(user, language);
    }

    public boolean exists(int id) {
        return textRepository.existsById(id);
    }

    public void save(Text text) {
        textRepository.save(text);
    }

    public void delete(int id) {
        textRepository.deleteById(id);
    }
}
