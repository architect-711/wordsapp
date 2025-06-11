package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import edu.architect_711.wordsapp.repository.AccountRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceIntegrationTest {
    @Autowired
    private DefaultAccountService defaultAccountService;
    @Autowired
    private AccountRepository accountRepository;

    public final SaveAccountDto UNEXISTING_ACCOUNT = new SaveAccountDto("test_" +LocalDateTime.now(), "1234", LocalDateTime.now() + "_name@domain.tld");

    private AccountDto savedBuff;

    @Test
    @Order(1)
    public void should_ok__save_new() {
        savedBuff = assertDoesNotThrow(() -> defaultAccountService.save(UNEXISTING_ACCOUNT));
        assertDoesNotThrow(() -> accountRepository.getReferenceById(savedBuff.getId()));
    }
    @Test
    @Order(2)
    public void should_fail__save_new__already_exists() {
        assertThrows(DataIntegrityViolationException.class, () -> defaultAccountService.save(UNEXISTING_ACCOUNT));
    }
    @Test
    @Order(3)
    public void should_fail__save_new__illegal_arguments() {
        SaveAccountDto invalid = new SaveAccountDto("   ", null, null);
        assertThrows(ConstraintViolationException.class, () -> defaultAccountService.save(invalid));
    }



    @Test @Order(4)
    @Transactional
    public void should_ok__get_saved_by_id() {
        AccountDto account = assertDoesNotThrow(() -> defaultAccountService.get(savedBuff.getId()));

        assertNotNull(account);
        assertEquals(savedBuff.getId(), account.getId());
        assertNotNull(account.getUsername());
        assertNotNull(account.getEmail());
    }
    @Test @Order(5)
    @Transactional
    public void should_fail__get_saved_by_id__illegal_arguments() {
        assertThrows(ConstraintViolationException.class, () -> defaultAccountService.get(-1L));
    }



    @Test @Order(6)
    @Transactional
    public void should_ok__update_saved() {
        AccountDto newOne = new AccountDto(savedBuff.getId(), "new_username", "new_email");

        AccountDto accountDto = assertDoesNotThrow(() -> defaultAccountService.update(newOne));

        assertNotNull(accountDto);
        assertEquals(savedBuff.getId(), accountDto.getId());
        assertEquals(newOne.getUsername(), accountDto.getUsername());
        assertEquals(newOne.getEmail(), accountDto.getEmail());
    }
    @Test @Order(7)
    public void should_fail__update_saved__illegal_arguments() {
        AccountDto newOne = new AccountDto(savedBuff.getId(), "     ", null);
        assertThrows(ConstraintViolationException.class, () -> defaultAccountService.update(newOne));

        newOne.setId(-1L);

        assertThrows(ConstraintViolationException.class, () -> defaultAccountService.update(newOne));
    }



    @Test @Order(8)
    public void should_ok__delete_saved() {
        assertDoesNotThrow(() -> defaultAccountService.delete(savedBuff.getId()));
    }
    @Test @Order(9)
    public void should_fail__delete_unreal__illegal_arguments() {
        assertThrows(ConstraintViolationException.class, () -> defaultAccountService.delete(-1L));
    }
}
