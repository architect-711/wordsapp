package edu.architect_711.wordsapp.service.account;

import edu.architect_711.wordsapp.model.dto.account.*;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.mapper.AccountMapper;
import edu.architect_711.wordsapp.repository.AccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static edu.architect_711.wordsapp.generator.Base64Generator.generate;
import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toEntity;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAccount;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.safeGetAccount;

@Service
@RequiredArgsConstructor
@Validated
public class DefaultAccountService implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountDto save(@Valid SaveAccountDto accountDto) {
        Account account = toEntity(accountDto);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        return toDto(accountRepository.save(account));
    }

    @Override
    public AuthBaseTokenResponse save64(@Valid SaveAccountDto saveAccountDto) {
        save(saveAccountDto);

        return buildAuthResponse(saveAccountDto.getUsername(), saveAccountDto.getPassword());
    }

    @Override
    public AuthBaseTokenResponse login64(@Valid AccountLoginRequest accountLoginRequest) {
        Account account = accountRepository.safeFindByUsername(accountLoginRequest.getUsername());

        if (!passwordEncoder.matches(accountLoginRequest.getPassword(), account.getPassword()))
            throw new BadCredentialsException("Wrong password");

        return buildAuthResponse(account.getUsername(), accountLoginRequest.getPassword());
    }

    private AuthBaseTokenResponse buildAuthResponse(String username, String password) {
        return new AuthBaseTokenResponse(generate(username, password));
    }

    @Override
    public Optional<AccountDto> get() {
        return Optional.ofNullable(getAccount()).map(AccountMapper::toDto);
    }

    @Override
    public void delete() {
        accountRepository.deleteById(safeGetAccount().getId());
        SecurityContextHolder.clearContext();
    }

    @Override
    public AccountDto update(@Valid UpdateAccountRequest accountDto) {
        Account account = safeGetAccount();

        account.setEmail(accountDto.getEmail());
        account.setUsername(accountDto.getUsername());

        return toDto(accountRepository.save(account));
    }

}
