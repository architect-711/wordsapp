package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Language;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {

   default Language safeFindById(Long languageId) {
       return findById(languageId).orElseThrow(() -> new EntityNotFoundException("Language not found by id: " + languageId));
   }

    /**
     * Find N-th element in the database
     * @param n the element order. MUST be >= 0
     * @return found lang
     */
   default Language findN(int n) throws IllegalArgumentException {
       return findAll(PageRequest.of(n, 1)).getContent().getFirst();
   }

}
