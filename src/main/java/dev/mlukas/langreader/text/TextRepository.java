package dev.mlukas.langreader.text;

import dev.mlukas.langreader.language.Language;
import dev.mlukas.langreader.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface TextRepository extends JpaRepository<Text, Integer> {
    List<Text> findAllByUserAndLanguageOrderByIdDesc(User user, Language language);
}
