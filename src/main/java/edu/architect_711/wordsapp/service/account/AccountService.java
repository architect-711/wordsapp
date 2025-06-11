package edu.architect_711.wordsapp.service.account;

import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface AccountService {
    /**
     * Get account by id
     * @param id account id
     * @return found account
     * @throws jakarta.persistence.EntityNotFoundException if not found
     */
    AccountDto get(@Min(0) Long id);

    /**
     * Delete account by id
     * @param id account id to be deleted
     * @throws IllegalArgumentException if given id is null
     */
    void delete(@Min(0) Long id);

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
