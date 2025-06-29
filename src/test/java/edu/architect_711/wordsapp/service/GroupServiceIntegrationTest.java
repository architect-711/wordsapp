package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.exception.UnauthorizedGroupModifyAttemptException;
import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import edu.architect_711.wordsapp.repository.AccountRepository;
import edu.architect_711.wordsapp.repository.GroupRepository;
import edu.architect_711.wordsapp.repository.LanguageRepository;
import edu.architect_711.wordsapp.repository.NodeRepository;
import edu.architect_711.wordsapp.repository.WordRepository;
import edu.architect_711.wordsapp.service.account.DefaultAccountService;
import edu.architect_711.wordsapp.service.group.GroupService;
import edu.architect_711.wordsapp.service.word.WordService;
import edu.architect_711.wordsapp.utils.Cleaner;
import edu.architect_711.wordsapp.utils.Persister;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toSaveGroupDto;
import static edu.architect_711.wordsapp.model.mapper.WordMapper.toEntity;
import static edu.architect_711.wordsapp.model.mapper.WordMapper.toSaveRequest;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAccount;
import static edu.architect_711.wordsapp.utils.Authenticator.authenticate;
import static edu.architect_711.wordsapp.utils.TestEntityGenerator.account;
import static edu.architect_711.wordsapp.utils.TestEntityGenerator.group;
import static edu.architect_711.wordsapp.utils.TestEntityGenerator.updateGroupDto;
import static edu.architect_711.wordsapp.utils.TestUtils.safeCleanAuth;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupServiceIntegrationTest {
    @Autowired
    private GroupService groupService;
    @Autowired
    private DefaultAccountService defaultAccountService;
    @Autowired
    private WordService wordService;

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

    private Persister persister;
    private Cleaner cleaner;

    @BeforeAll
    public void setup() {
        persister = Persister.builder()
                .accountRepository(accountRepository)
                .accountService(defaultAccountService)
                .groupService(groupService)
                .wordService(wordService)
                .build();
        cleaner = Cleaner.builder()
                .groupRepository(groupRepository)
                .accountRepository(accountRepository)
                .build();
    }

    @AfterEach
    public void cleanup() {
        safeCleanAuth();
    }

    /* --------------- SAVE --------------- */
    @Test
    public void should_ok__save() {
        // prepare
        var account = persister.save_auth_get_account();

        // persist
        var model = group(account);
        GroupDto group = assertDoesNotThrow(() -> groupService.save(toSaveGroupDto(model)));

        // check
        assertTrue(groupRepository.findById(group.getId()).isPresent());

        assertNotNull(group);

        assertEquals(model.getTitle(), group.getTitle());
        assertEquals(model.getDescription(), group.getDescription());

        // cleanup
        cleaner.clear(group.getId(), account.getId());
    }

    @Test
    public void should_fail__save__illegal_arguments() {
        // prepare
        var account = persister.save_auth_get_account();

        // check
        var invalid = new SaveGroupDto("         \n ", null);

        assertThrows(ConstraintViolationException.class, () -> groupService.save(invalid));

        // cleanup
        accountRepository.deleteById(account.getId());
    }

    /* --------------- GET --------------- */
    @Test
    public void should_ok__get_all() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());

        // check
        assertNotNull(group);

        var found = assertDoesNotThrow(() -> groupService.get());

        assertNotNull(found);

        assertTrue(found.size() >= 1);
        found.forEach(f -> assertEquals(f.getOwnerId(), getAccount().getId()));

        // cleanup
        cleaner.clear(group.getId(), account.getId());
    }

    /* --------------- UPDATE --------------- */
    @Test
    public void should_ok__update() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());

        // persist
        var update = updateGroupDto(group.getId());
        var updated = assertDoesNotThrow(() -> groupService.update(update));

        // check
        assertNotNull(updated);

        assertEquals(updated.getTitle(), update.getTitle());
        assertEquals(updated.getDescription(), update.getDescription());

        assertEquals(getAccount().getId(), updated.getOwnerId());

        // cleanup
        cleaner.clear(group.getId(), account.getId());
    }

    @Test
    public void should_fail__update__fabulous_id() {
        // prepare
        var account = persister.save_auth_get_account();

        // check
        final Long REALLY_FABULOUS_ID = 999999L;
        var invalid = new UpdateGroupDto(
                REALLY_FABULOUS_ID,
                "this should never be saved in the database",
                "if it did, checkout the test files");

        // since test firstly tries to find group by id, we don't have to save it before
        assertThrows(EntityNotFoundException.class, () -> groupService.update(invalid));

        var found = groupRepository.findByTitle(invalid.getTitle());
        assertTrue(found.isEmpty());

        // cleanup
        accountRepository.deleteById(account.getId());
    }

    @Test
    public void should_fail__update__illegal_arguments() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // check
        var invalid = new UpdateGroupDto(-1L, null, "                   \0  ");

        Set<ConstraintViolation<UpdateGroupDto>> validateResult = validator.validate(invalid);
        assertFalse(validateResult.isEmpty());
        assertEquals(3, validateResult.size());

        assertThrows(ConstraintViolationException.class, () -> groupService.update(invalid));

        // check wasn't updated
        var found = groupRepository.findById(group.getId());
        assertTrue(found.isPresent());
        assertEquals(group.getTitle(), found.get().getTitle());

        // cleanup
        cleaner.clear(group.getId(), account.getId());
    }

    @Test
    public void should_fail__update__third_party_access() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());

        // login as another user
        var accountModel2 = account();
        var account2 = assertDoesNotThrow(() -> accountRepository.save(accountModel2));
        authenticate(account2);

        // check
        var firstAccountGroupToUpdate = updateGroupDto(group.getId());

        assertThrows(
                UnauthorizedGroupModifyAttemptException.class,
                () -> groupService.update(firstAccountGroupToUpdate));

        // cleanup
        cleaner.clear(group.getId(), account.getId(), account2.getId());
    }

    /* --------------- DELETE --------------- */
    @Test
    public void should_ok__delete() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());

        // check
        assertDoesNotThrow(() -> groupService.delete(group.getId()));

        assertTrue(groupRepository.findById(group.getId()).isEmpty());

        // cleanup
        accountRepository.deleteById(account.getId());
    }

    @Test
    public void should_fail__delete__third_party_access() {
        // prepare
        var account = persister.save_auth_get_account();
        var group = persister.safe_persist_group(getAccount());

        // login as another user
        var accountModel2 = account();
        var account2 = assertDoesNotThrow(() -> accountRepository.save(accountModel2));
        authenticate(account2);

        // check
        assertThrows(
                UnauthorizedGroupModifyAttemptException.class,
                () -> groupService.delete(group.getId()));

        assertTrue(groupRepository.findById(group.getId()).isPresent());

        // cleanup
        cleaner.clear(group.getId(), account.getId(), account2.getId());
    }

    /*
     * There are 2 groups with the same word. When calling the `#delete` group
     * method, it MUST delete only requested group, its nodes and all related words,
     * which don't present in other groups.
     * <p>
     * So here, after `groupToBeDeleted` deletion, the `groupToBeLive` must still
     * point to the `sharedWord`,
     * while `groupToBeDeleted` doesn't, because it was deleted and the `uniqueWord`
     * was deleted as well.
     */
    @Transactional
    @Test
    public void should_ok__delete__with_node_refs() {
        // prepare
        var account = persister.save_auth_get_account();

        // our 2 groups
        var groupToBeDeleted = persister.safe_persist_group(getAccount());
        var groupToBeLive = persister.safe_persist_group(getAccount());

        var lang = languageRepository.findN(0);

        // victim group
        long groupIdToBeDeleted = groupToBeDeleted.getId();

        // with the same word
        var shareWord = persister.safe_persist_word(lang, groupIdToBeDeleted);
        var shareWordCopy = wordService.save(
                toSaveRequest(toEntity(shareWord, lang)),
                groupToBeLive.getId());

        // will be deleted
        var uniqueWord = persister.safe_persist_word(lang, groupIdToBeDeleted);

        // and 2 nodes created by the WordService
        var nodes = nodeRepository.findAllByWordId(shareWord.getId());

        // check
        assertEquals(2, nodes.size());

        // words must be the same
        compareWords(shareWord, shareWordCopy);

        assertDoesNotThrow(() -> groupService.delete(groupIdToBeDeleted));

        assertFalse(groupRepository.existsById(groupIdToBeDeleted));
        assertTrue(groupRepository.existsById(groupToBeLive.getId()));

        assertTrue(nodeRepository.findByGroupId(groupIdToBeDeleted).isEmpty());
        assertTrue(nodeRepository.findByGroupId(groupToBeLive.getId()).isPresent());

        assertFalse(wordRepository.existsById(uniqueWord.getId()));
        assertTrue(wordRepository.existsById(shareWord.getId()));
        assertTrue(wordRepository.existsById(shareWordCopy.getId())); // for any case

        // cleanup
        assertDoesNotThrow(() -> groupService.delete(groupToBeLive.getId()));
        accountRepository.deleteById(account.getId());
    }

    private void compareWords(WordDto a, WordDto b) {
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getTitle(), b.getTitle());
    }
}
