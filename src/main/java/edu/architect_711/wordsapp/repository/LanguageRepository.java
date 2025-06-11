package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
