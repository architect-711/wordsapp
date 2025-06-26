package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.model.dto.account.AccountDto;
import edu.architect_711.wordsapp.model.dto.account.SaveAccountDto;
import edu.architect_711.wordsapp.repository.AccountRepository;
import edu.architect_711.wordsapp.service.account.DefaultAccountService;
import edu.architect_711.wordsapp.utils.Persister;
import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toSaveAccountDto;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAuthentication;
import static edu.architect_711.wordsapp.utils.TestUtils.safeCleanAuth;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountServiceIntegrationTest {
    @Autowired
    private DefaultAccountService defaultAccountService;
    @Autowired
    private AccountRepository accountRepository;

    private Persister persister;

    @BeforeAll
    public void setup() {
        persister = Persister.builder()
                .accountRepository(accountRepository)
                .accountService(defaultAccountService)
                .build();
    }

    @AfterEach
    public void cleanup() {
        safeCleanAuth();
    }

    /* --------------- SAVE --------------- */
    @Test
    public void should_ok__save() {
        // persist
        var account = persister.safe_persist_account();

        // cleanup
        accountRepository.deleteById(account.getId());
    }

    @Test
    public void should_fail__save__already_exists() {
        // persist
        var account = persister.safe_persist_account();

        // check
        assertThrows(
                DataIntegrityViolationException.class,
                () -> defaultAccountService.save(toSaveAccountDto(account)));

        // cleanup
        accountRepository.deleteById(account.getId());
    }

    @Test
    public void should_fail__save__illegal_arguments() {
        // model
        var invalid = new SaveAccountDto("   ", null, null);

        // check
        assertThrows(ConstraintViolationException.class, () -> defaultAccountService.save(invalid));

        var found = accountRepository.findByUsername(invalid.getUsername());
        assertTrue(found.isEmpty());
    }

    /* --------------- GET --------------- */
    @Test
    public void should_ok__get() {
        // persist
        var saved = persister.save_auth_get_account();

        // check
        AccountDto account = assertDoesNotThrow(() -> defaultAccountService.get());

        assertNotNull(account);
        assertNotNull(account.getUsername());
        assertNotNull(account.getEmail());

        assertEquals(saved.getId(), account.getId());
        assertEquals(saved.getUsername(), account.getUsername());
        assertEquals(saved.getEmail(), account.getEmail());

        // cleanup
        accountRepository.deleteById(saved.getId());
    }

    /* --------------- UPDATE --------------- */
    @Test
    public void should_ok__update() {
        // persist
        var saved = persister.save_auth_get_account();

        var updateRequest = new AccountDto(saved.getId(), "new_username_" + LocalDateTime.now(), "new_email");

        // check
        AccountDto updateResponse = assertDoesNotThrow(() -> defaultAccountService.update(updateRequest));

        assertNotNull(updateRequest);

        assertEquals(saved.getId(), updateResponse.getId());
        assertEquals(saved.getUsername(), updateResponse.getUsername());
        assertEquals(saved.getEmail(), updateResponse.getEmail());

        // cleanup
        accountRepository.deleteById(saved.getId());
    }

    @Test
    public void should_fail__update__illegal_arguments() {
        // persist
        var saved = persister.save_auth_get_account();

        // check
        AccountDto invalid = new AccountDto(saved.getId(), "     ", null);
        assertThrows(ConstraintViolationException.class, () -> defaultAccountService.update(invalid));

        invalid.setId(-1L);

        assertThrows(ConstraintViolationException.class, () -> defaultAccountService.update(invalid)); // just for fun

        // cleanup
        accountRepository.deleteById(saved.getId());
    }

    /* --------------- DELETE --------------- */
    @Test
    public void should_ok__delete() {
        // persist
        var saved = persister.save_auth_get_account();

        // check
        assertDoesNotThrow(() -> defaultAccountService.delete());

        var found = accountRepository.findById(saved.getId());
        assertTrue(found.isEmpty());

        assertNull(getAuthentication());
    }
}
