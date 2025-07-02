package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.exception.UnauthorizedGroupModifyAttemptException;
import edu.architect_711.wordsapp.model.dto.word.SaveWordRequest;
import edu.architect_711.wordsapp.model.dto.word.UpdateWordRequest;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import edu.architect_711.wordsapp.model.entity.Word;
import edu.architect_711.wordsapp.repository.*;
import edu.architect_711.wordsapp.service.account.AccountService;
import edu.architect_711.wordsapp.service.group.GroupService;
import edu.architect_711.wordsapp.service.word.DefaultWordService;
import edu.architect_711.wordsapp.utils.Cleaner;
import edu.architect_711.wordsapp.utils.Persister;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static edu.architect_711.wordsapp.model.mapper.WordMapper.*;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAccount;
import static edu.architect_711.wordsapp.utils.TestEntityGenerator.word;
import static edu.architect_711.wordsapp.utils.TestUtils.safeCleanAuth;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class WordServiceIntegrationTest {
    @Autowired
    private DefaultWordService wordService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private NodeRepository nodeRepository;

    private Cleaner cleaner;
    private Persister persister;

    @BeforeAll
    public void setup() {
        cleaner = Cleaner.builder()
                .groupRepository(groupRepository)
                .accountRepository(accountRepository)
                .nodeRepository(nodeRepository)
                .wordRepository(wordRepository)
                .build();

        persister = Persister.builder()
                .accountRepository(accountRepository)
                .accountService(accountService)
                .groupService(groupService)
                .wordService(wordService)
                .build();
    }

    @AfterEach
    public void cleanup() {
        safeCleanAuth();
    }

    /* --------------- SAVE --------------- */
    @Test
//     @Transactional
    public void should_ok__save() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());
        var lang = languageRepository.findN(0);

        // persist
        var model = word(lang);
        var word = assertDoesNotThrow(() -> wordService.save(toSaveRequest(model), group.getId()));

        // check
        assertNotNull(word);

        compare(model, word);

        assertNotNull(word.getId());
        assertNotNull(word.getCreated());

        // find the node
        var node = assertDoesNotThrow(() -> nodeRepository.safeFindByWordId(word.getId()));

        // check whether the node was saved properly
        assertNotNull(node);

        assertEquals(node.getWord().getId(), word.getId());
        assertEquals(node.getGroup().getId(), group.getId());

        // relationship check. check group has a reference
        var groupEntity = groupRepository.findById(group.getId());
        var wordEntity = wordRepository.findById(word.getId());

        assertTrue(groupEntity.isPresent());
        assertTrue(wordEntity.isPresent());

        var words = groupEntity.get().getWords();

        assertFalse(words.isEmpty());

        var equals = false;
        for (Word w : words) {
            if (w.getId().equals(wordEntity.get().getId())) {
                compare(wordEntity.get(), toDto(w));
                equals = true;
            }
        }
        assertTrue(equals);

        // cleanup
        cleaner.clear(node.getId(), word.getId(), group.getId(), account.getId());
    }

    private void compare(Word a, WordDto b) {
        assertEquals(a.getTitle(), b.getTitle());
        assertEquals(a.getDefinition(), b.getDefinition());
        assertEquals(a.getDescription(), b.getDescription());
        assertEquals(a.getTranscriptions(), b.getTranscriptions());
        assertEquals(a.getTranslations(), b.getTranslations());
        assertEquals(a.getLanguage().getId(), b.getLanguageId());
    }

    @Test
    public void should_fail__save__illegal_arguments() {
        // check
        var invalid = new SaveWordRequest(
                null, -1L, "      \t", "", null, null);

        // number of constraints
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<SaveWordRequest>> validateResult = validator.validate(invalid);

        // why 2? see the SaveWordRequest!
        assertEquals(2, validateResult.size());

        // service reject
        assertThrows(
                ConstraintViolationException.class,
                () -> wordService.save(invalid, 0L)); // don't care about group id

        // wasn't saved
        var found = wordRepository.findByTitle(invalid.getTitle());

        assertTrue(found.isEmpty());
    }

    // when trying to save a word into another user group
    @Test
    public void should_fail__save__third_party_access() {
        // prepare
        var user1 = persister.save_auth_get_account();
        var user1Group = persister.safe_persist_group(getAccount());
        var lang = languageRepository.findN(0);

        var word = word(lang);

        // login as another user
        var user2 = persister.save_auth_get_account();

        // check
        assertThrows(
                UnauthorizedGroupModifyAttemptException.class,
                () -> wordService.save(toSaveRequest(word), user1Group.getId()));

        // check wasn't save
        var foundWord = wordRepository.findByTitle(word.getTitle());
        var foundNode = nodeRepository.findByWordId(word.getId());

        assertTrue(foundWord.isEmpty());
        assertTrue(foundNode.isEmpty());

        // cleanup
        cleaner.clear(user1Group.getId(), user1.getId());
        accountRepository.deleteById(user2.getId());
    }

    /* --------------- GET BY ID --------------- */
    @Test
    public void should_ok__get_by_id() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());
        var lang = languageRepository.findN(0);
        var word = persister.safe_persist_word(lang, group.getId());

        // check
        var found = assertDoesNotThrow(() -> wordService.get(word.getId()));
        assertTrue(found.isPresent());
        compare(toEntity(word, lang), found.get());

        // check node
        var node = nodeRepository.findByWordId(word.getId());

        assertTrue(node.isPresent());
        assertEquals(group.getId(), node.get().getGroup().getId());

        // cleanup
        cleaner.clear(node.get().getId(), word.getId(), group.getId(), account.getId());
    }

    @Test
    public void should_fail__get_by_id__not_found() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());

        var fabulousWordId = 9999999L;

        // check
        var found = assertDoesNotThrow(() -> wordService.get(fabulousWordId));

        assertTrue(found.isEmpty());

        // cleanup
        cleaner.clear(group.getId(), account.getId());
    }

    /* --------------- GET ALL --------------- */
    @Test
    public void should_ok__get_all() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());
        var lang = languageRepository.findN(0);

        // persist
        var word1 = persister.safe_persist_word(lang, group.getId());
        var word2 = persister.safe_persist_word(lang, group.getId());

        // check
        var node1 = nodeRepository.findByWordId(word1.getId());
        var node2 = nodeRepository.findByWordId(word2.getId());

        assertTrue(node1.isPresent());
        assertTrue(node2.isPresent());

        // this might fail if the database is already contained some words, don't really
        // care about
        var found = assertDoesNotThrow(() -> wordService.get(0, 2, group.getId()));

        assertFalse(found.isEmpty());
        assertEquals(2, found.size());

        var timesMatched = 0;
        for (WordDto f : found) {
            if (f.getTitle().equals(word1.getTitle()) || f.getTitle().equals(word2.getTitle()))
                timesMatched++;
        }
        assertEquals(2, timesMatched);

        // cleanup
        nodeRepository.deleteById(node1.get().getId());
        nodeRepository.deleteById(node2.get().getId());

        wordRepository.deleteById(word1.getId());
        wordRepository.deleteById(word2.getId());

        cleaner.clear(group.getId(), account.getId());
    }

    /* --------------- DELETE --------------- */
    @Test
    @Transactional
    public void should_ok__delete_from_group() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());
        var lang = languageRepository.findN(0);
        var word = persister.safe_persist_word(lang, group.getId());
        var node = nodeRepository.findAllByWordIdAndGroupId(word.getId(), group.getId());

        // check
        assertEquals(1, node.size());
        assertDoesNotThrow(() -> wordService.delete(word.getId(), group.getId()));

        assertTrue(nodeRepository.findAllByWordIdAndGroupId(word.getId(), group.getId()).isEmpty());

        boolean wordExists = wordRepository.existsById(word.getId());
        if (wordRepository.count() == 1)
            assertTrue(wordExists);
        else
            assertFalse(wordExists);

        assertTrue(groupRepository.existsById(group.getId()));

        // clean
        cleaner.clear(node.getFirst().getId(), word.getId(), group.getId(), account.getId());
    }

    /* --------------- UPDATE --------------- */
    @Test
    @Transactional
    public void should_ok__update() {
        // prepare
        var account = persister.save_auth_get_account();
        var lang = languageRepository.findN(0);
        var group = persister.safe_persist_group(getAccount());
        var word = word(lang);

        // persist
        var saved = assertDoesNotThrow(() -> wordService.save(toSaveRequest(word), group.getId()));

        assertNotNull(saved);

        // check
        var newOne = word(lang);
        newOne.setId(saved.getId());

        var updated = assertDoesNotThrow(() -> wordService.update(toUpdateWordRequest(newOne)));

        compareWordsExact(newOne, updated);

        // cleanup
        cleaner.clear(group.getId(), account.getId());
        wordRepository.deleteById(updated.getId());
    }

    private void compareWordsExact(Word a, WordDto b) {
        assertNotNull(a);
        assertNotNull(b);

        assertEquals(a.getTitle(), b.getTitle());
        assertEquals(a.getLanguage().getId(), b.getLanguageId());
        assertEquals(a.getDefinition(), b.getDefinition());
        assertEquals(a.getDescription(), b.getDescription());
        assertEquals(a.getTranslations(), b.getTranslations());
        assertEquals(a.getTranscriptions(), b.getTranscriptions());
    }

    @Test
    @Transactional
    public void should_fail__update__illegal_arguments() {
        // prepare
        var account = persister.save_auth_get_account();
        var lang = languageRepository.findN(0);
        var group = persister.safe_persist_group(getAccount());
        var word = word(lang);

        // persist
        var saved = assertDoesNotThrow(() -> wordService.save(toSaveRequest(word), group.getId()));

        // check
        var invalid = new UpdateWordRequest(-1L, "    \n", null, null, "\0", null, null);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UpdateWordRequest>> validate = validator.validate(invalid);

        assertEquals(3, validate.size());

        assertThrows(ConstraintViolationException.class, () -> wordService.update(invalid));

        // cleanup
        cleaner.clear(group.getId(), account.getId());
        wordRepository.deleteById(saved.getId());
    }

}
