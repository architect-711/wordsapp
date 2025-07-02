package edu.architect_711.wordsapp.service.account;

import edu.architect_711.wordsapp.model.dto.account.*;
import jakarta.validation.Valid;

import java.util.Optional;

public interface AccountService {
    /**
     * Get account
     *
     * @return found account
     */
    Optional<AccountDto> get();

    /**
     * Login user and return base64 token
     *
     * @param accountLoginRequest credentials
     * @return base64 token
     */
    AuthBaseTokenResponse login64(@Valid AccountLoginRequest accountLoginRequest);

    /**
     * Delete account and clear the
     * {@link org.springframework.security.core.context.SecurityContextHolder}
     */
    void delete();

    /**
     * Save new account
     *
     * @param accountDto account to be saved
     * @return saved account
     */
    AccountDto save(@Valid SaveAccountDto accountDto);

    /**
     * Save new account and return base64 token
     *
     * @param accountDto new account
     * @return base64 token
     * @throws jakarta.validation.ConstraintViolationException if fails validation
     */
    AuthBaseTokenResponse save64(@Valid SaveAccountDto accountDto);

    /**
     * Update existing account (without password modification)
     *
     * @param accountDto new account
     * @return updated account
     */
    AccountDto update(@Valid UpdateAccountRequest accountDto);
}
