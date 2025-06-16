package edu.architect_711.wordsapp.service.account;

import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.LoginRequest;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.repository.AccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Base64;

import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toEntity;
import static edu.architect_711.wordsapp.security.AuthenticationExtractor.getAccount;

@Service @RequiredArgsConstructor
@Validated
public class DefaultAccountService implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountDto save(@Valid SaveAccountDto accountDto) { // TODO mb return base64 token?
        Account account = toEntity(accountDto);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        return toDto(accountRepository.save(account));
    }

    @Override
    public String login64(LoginRequest loginRequest) {
        Account account = accountRepository.safeFindByUsername(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        String credentials = account.getUsername() + ":" + loginRequest.getPassword();

        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    @Override
    public AccountDto get() {
        return toDto(getAccount());
    }

    @Override
    public void delete() {
        accountRepository.deleteById(getAccount().getId());
        SecurityContextHolder.clearContext();
    }

    @Override
    public AccountDto update(@Valid AccountDto accountDto) {
        Account account = getAccount();

        account.setEmail(accountDto.getEmail());
        account.setUsername(accountDto.getUsername());

        return toDto(accountRepository.save(account));
    }


}
