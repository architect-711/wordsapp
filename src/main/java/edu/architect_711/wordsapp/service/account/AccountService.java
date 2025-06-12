package edu.architect_711.wordsapp.service.account;

import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.LoginRequest;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import jakarta.validation.Valid;

public interface AccountService {
    /**
     * Get account, this is the private endpoint, so
     * developer must retrieve info from the {@link org.springframework.security.core.Authentication}
     * @return found account
     * @throws jakarta.persistence.EntityNotFoundException if not found
     */
    AccountDto get();

    /**
     * Authenticate a user by generating base64 token
     * @param loginRequest credentials
     * @return base64 token
     */
    String login64(@Valid LoginRequest loginRequest);

    /**
     * Delete account, this is the private endpoint, so
     * developer must retrieve info from the {@link org.springframework.security.core.Authentication}
     * @param id account id to be deleted
     * @throws IllegalArgumentException if given id is null
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
