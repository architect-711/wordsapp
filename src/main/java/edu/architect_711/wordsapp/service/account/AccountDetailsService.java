package edu.architect_711.wordsapp.service.account;

import edu.architect_711.wordsapp.model.dto.account.AccountDetails;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.safeFindByUsername(username);

        return new AccountDetails(account);
    }
}
