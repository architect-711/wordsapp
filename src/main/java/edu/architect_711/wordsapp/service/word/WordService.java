package edu.architect_711.wordsapp.service.word;

import edu.architect_711.wordsapp.model.dto.word.SaveWordRequest;
import edu.architect_711.wordsapp.model.dto.word.UpdateWordRequest;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface WordService {

    /**
     * Save new word, create a node.
     * Otherwise, nothing should happen, so consider to make
     * it {@link jakarta.transaction.Transactional}-annotated
     *
     * @param saveWordRequest new word to be saved
     * @param groupId         the group id to be saved in
     * @return saved word
     */
    WordDto save(@Valid SaveWordRequest saveWordRequest, Long groupId);

    /**
     * Find words in the group paginated
     *
     * @param page    page
     * @param size    size
     * @param groupId group id
     * @return Found words
     */
    List<WordDto> get(int page, int size, Long groupId);

    /**
     * Get one word from a group by id
     *
     * @param wordId word id
     * @return found word
     */
    Optional<WordDto> get(Long wordId);

    /**
     * Delete a word from a group. It means to remove just a node.
     *
     * @param wordId  word id to be removed
     * @param groupId from group id
     */
    void delete(Long wordId, Long groupId);

    /**
     * Usually for internal purposes.
     * <p>
     * Delete permanently a word from table. Make sure, that the node table
     * hasn't got any references to the word you are trying to delete, otherwise
     * you get the exception
     *
     * @param wordId word id to be deleted forever
     */
    void delete(Long wordId);

    /**
     * Updates the word
     *
     * @param updateWordRequest word to be updated
     * @return updated word
     */
    WordDto update(@Valid UpdateWordRequest updateWordRequest);
}
