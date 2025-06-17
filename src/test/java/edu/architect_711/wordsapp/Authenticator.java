package edu.architect_711.wordsapp;

import edu.architect_711.wordsapp.model.dto.account.AccountDetails;
import edu.architect_711.wordsapp.model.entity.Account;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Authenticator {
    /**
     * Mock actual authentication mechanism, since it doesn't matter here
     * @param account the account that must exist
     */
    public static void authenticate(Account account) {
        assertNotNull(account);

        var accountDetails = new AccountDetails(account);

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(accountDetails,
                accountDetails.getPassword(), List.of());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        assertNotNull(SecurityContextHolder.getContext());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
