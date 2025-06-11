package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.repository.AccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toEntity;

@Service @RequiredArgsConstructor
@Validated
public class DefaultAccountService implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public AccountDto save(@Valid SaveAccountDto accountDto) {
        return toDto(accountRepository.save(toEntity(accountDto)));
    }

    @Override
    public AccountDto get(Long id) {
        return toDto(accountRepository.getReferenceById(id)); // TODO ???
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public AccountDto update(@Valid AccountDto accountDto) {
        Account account = accountRepository.getReferenceById(accountDto.getId());

        account.setEmail(accountDto.getEmail());
        account.setUsername(accountDto.getUsername());

        return toDto(accountRepository.save(account));
    }
}
