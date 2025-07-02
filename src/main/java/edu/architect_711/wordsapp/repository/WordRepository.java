package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Word;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByTitle(String title);

    default Word safeFindById(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Word not found with id: " + id));
    }

}
