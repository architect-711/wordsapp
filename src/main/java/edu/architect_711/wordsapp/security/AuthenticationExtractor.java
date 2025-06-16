package edu.architect_711.wordsapp.security;

import edu.architect_711.wordsapp.model.dto.AccountDetails;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationExtractor {
    private final AccountRepository accountRepository;

    /**
     * retrieve authenticated user from the context and database consequently
     * @return found account
     * @throws EntityNotFoundException if the account wasn't found
     */
    public Account extract() {
        AccountDetails principal = getUserDetailsFromAuthContext();

        return accountRepository.safeFindById(principal.getId());
    }

    public AccountDetails getUserDetailsFromAuthContext() {
        return (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
