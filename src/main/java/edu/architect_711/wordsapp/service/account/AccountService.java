package edu.architect_711.wordsapp.service.account;

import edu.architect_711.wordsapp.model.dto.account.AccountDto;
import edu.architect_711.wordsapp.model.dto.account.AccountLoginRequest;
import edu.architect_711.wordsapp.model.dto.account.SaveAccountDto;
import jakarta.validation.Valid;

public interface AccountService {
    /**
     * Get account
     * @return found account
     * @throws jakarta.persistence.EntityNotFoundException if not found // TODO why? why not optional?
     */
    AccountDto get();

    /**
     * Login user and generate a base64 token in return for user to allow
     * to be authenticated
     * @param accountLoginRequest credentials
     * @return base64 token
     */
    String login64(@Valid AccountLoginRequest accountLoginRequest);

    /**
     * Delete account and clears the {@link org.springframework.security.core.context.SecurityContextHolder}
     */
    void delete();

    /**
     * Saves new account
     * @param accountDto new account
     * @return saved account
     * @throws jakarta.validation.ConstraintViolationException if fails validation
     */
    AccountDto save(@Valid SaveAccountDto accountDto);

    /**
     * Update existing account (without password modification)
     * @param accountDto new account
     * @return updated account
     * @throws jakarta.persistence.EntityNotFoundException if account not found
     */
    AccountDto update(@Valid AccountDto accountDto);
}
