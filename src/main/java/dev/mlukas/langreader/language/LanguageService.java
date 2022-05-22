package dev.mlukas.langreader.language;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public Language getLanguage(int id) {
        return languageRepository.findById(id)
                .orElseThrow(() ->
                        new LanguageNotFoundException("The language with the id '%d' doesn't exist.".formatted(id))
                );
    }

    public Language getLanguage(String code) {
        return languageRepository.findByCode(code)
                .orElseThrow(() ->
                        new LanguageNotFoundException("The language with the code '%s' doesn't exist.".formatted(code))
                );
    }

    public Language getLanguageByCodeAndFullName(String code, String fullName) {
        return languageRepository.findByCodeAndFullName(code, fullName)
                .orElseThrow(() ->
                        new LanguageNotFoundException("The language with the code '%s' and full name '%s' doesn't exist.".formatted(code, fullName))
                );
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }
}
