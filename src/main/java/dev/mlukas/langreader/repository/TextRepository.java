package dev.mlukas.langreader.repository;

import dev.mlukas.langreader.model.Language;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Integer> {
    List<Text> findAllByUserAndLanguageOrderByIdDesc(User user, Language language);
}
