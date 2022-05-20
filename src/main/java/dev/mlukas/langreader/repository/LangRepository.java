package dev.mlukas.langreader.repository;

import dev.mlukas.langreader.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LangRepository extends JpaRepository<Language, Integer> {
    Language findById(int id);

    Language findByCode(String code);
}