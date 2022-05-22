package dev.mlukas.langreader.language;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface LanguageRepository extends JpaRepository<Language, Integer> {
    Optional<Language> findById(int id);

    Optional<Language> findByCode(String code);

    Optional<Language> findByCodeAndFullName(String code, String fullName);
}
