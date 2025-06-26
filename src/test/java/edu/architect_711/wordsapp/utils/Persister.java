package edu.architect_711.wordsapp.utils;

import edu.architect_711.wordsapp.model.dto.account.AccountDto;
import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.entity.Language;
import edu.architect_711.wordsapp.repository.AccountRepository;
import edu.architect_711.wordsapp.service.account.AccountService;
import edu.architect_711.wordsapp.service.group.GroupService;
import edu.architect_711.wordsapp.service.word.WordService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toSaveGroupDto;
import static edu.architect_711.wordsapp.model.mapper.WordMapper.toSaveRequest;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAuthentication;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getContext;
import static edu.architect_711.wordsapp.utils.Authenticator.authenticate;
import static edu.architect_711.wordsapp.utils.TestEntityGenerator.group;
import static edu.architect_711.wordsapp.utils.TestEntityGenerator.saveAccountDto;
import static edu.architect_711.wordsapp.utils.TestEntityGenerator.word;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
@Builder
public class Persister {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final GroupService groupService;
    private final WordService wordService;

    public Account safe_persist_account() {
        // persist
        var model = saveAccountDto();
        AccountDto saved = assertDoesNotThrow(() -> accountService.save(model));

        // check
        assertNotNull(saved);

        assertEquals(model.getEmail(), saved.getEmail());
        assertEquals(model.getUsername(), saved.getUsername());

        var found = accountRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        return found.get();
    }

    public Account save_auth_get_account() {
        var account = safe_persist_account();
        authenticate(account);

        assertNotNull(getContext());
        assertNotNull(getAuthentication());

        return account;
    }

    public GroupDto safe_persist_group(Account account) {
        var model = group(account);

        return assertDoesNotThrow(() -> groupService.save(toSaveGroupDto(model)));
    }

    public WordDto safe_persist_word(Language lang, Long groupId) {
        var model = word(lang);

        return assertDoesNotThrow(() -> wordService.save(toSaveRequest(model), groupId));
    }
}
