package edu.architect_711.wordsapp.utils;

import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.repository.AccountRepository;

import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.clearContext;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAuthentication;
import static edu.architect_711.wordsapp.utils.TestEntityGenerator.account;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestUtils {
    /**
     * Save new account either return account number N.
     * <p>
     * <b>Very important</b>, because sometimes we don't care
     * whether many accounts exist or not, we just want to get
     * another one.
     * 
     * @param accountN number of the account in the database, >= 0
     * @return saved or found account
     */
    public static Account getAccountAnyway(AccountRepository accountRepository, int accountN) {
        return accountRepository.count() < 2
                ? accountRepository.save(account())
                : accountRepository.safeFindN(accountN);
    }

    public static void safeCleanAuth() {
        clearContext();
        assertNull(getAuthentication());
    }
}
