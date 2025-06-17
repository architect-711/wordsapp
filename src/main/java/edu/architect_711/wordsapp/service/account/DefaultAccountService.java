package edu.architect_711.wordsapp.service.account;

import edu.architect_711.wordsapp.model.dto.account.AccountDto;
import edu.architect_711.wordsapp.model.dto.account.AccountLoginRequest;
import edu.architect_711.wordsapp.model.dto.account.SaveAccountDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.repository.AccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.AccountMapper.toEntity;
import static edu.architect_711.wordsapp.security.AuthenticatedUserExtractor.getAccount;
import static edu.architect_711.wordsapp.service.Base64Generator.generate;

@Service
@RequiredArgsConstructor
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
    public String login64(AccountLoginRequest accountLoginRequest) {
        Account account = accountRepository.safeFindByUsername(accountLoginRequest.getUsername());

        if (!passwordEncoder.matches(accountLoginRequest.getPassword(), account.getPassword()))
            throw new BadCredentialsException("Wrong password");

        return generate(account.getUsername(), account.getPassword());
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
