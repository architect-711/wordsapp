package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.model.dto.AccountDetails;
import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.repository.AccountRepository;
import edu.architect_711.wordsapp.service.account.DefaultAccountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toEntity;
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

    private void authenticate(Account account) {
        var accountDetails = new AccountDetails(account);

        // Note, that it works only when calling the service, because it doesn't care about unencrypted password
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(accountDetails, accountDetails.getPassword(), List.of()); 

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Test
    @Order(1)
    public void should_ok__save_new() {
        savedBuff = assertDoesNotThrow(() -> defaultAccountService.save(UNEXISTING_ACCOUNT));
        assertDoesNotThrow(() -> accountRepository.getReferenceById(savedBuff.getId()));

        authenticate(toEntity(savedBuff, UNEXISTING_ACCOUNT.getPassword()));
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
        AccountDto account = assertDoesNotThrow(() -> defaultAccountService.get());

        assertNotNull(account);
        assertEquals(savedBuff.getId(), account.getId());
        assertNotNull(account.getUsername());
        assertNotNull(account.getEmail());
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
        assertDoesNotThrow(() -> defaultAccountService.delete());

        assertThrows(EntityNotFoundException.class, () -> defaultAccountService.get());
    }
}
