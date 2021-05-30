package net.langreader.repository;

import net.langreader.model.Language;
import net.langreader.model.User;
import net.langreader.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Integer> {
    List<Text> findAllByUserAndLanguageOrderByIdDesc(User user, Language language);
}
