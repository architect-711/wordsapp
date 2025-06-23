package edu.architect_711.wordsapp.service.word;

import edu.architect_711.wordsapp.model.dto.word.SaveWordRequest;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface WordService {

    /**
     * Saves new word. This word must be saved in:
     * 1. its table
     * 2. the join table `group_words`
     * Otherwise nothing should happen, so consider to make
     * it {@link jakarta.transaction.Transactional}-annotated
     * 
     * @param saveWordRequest new word to be saved
     * @param groupId         the group id to be saved in
     * 
     * @return saved word
     */
    WordDto save(@Valid SaveWordRequest saveWordRequest, Long groupId);

    /**
     * Find all words in the group by id
     * 
     * @param groupId group id
     * @return found words
     */
    List<WordDto> get(int page, int size, Long groupId);

    /**
     * Get one word from a group by id
     * 
     * @param wordId word id
     * @return found word
     */
    Optional<WordDto> get(Long wordId);

}
