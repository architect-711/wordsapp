package edu.architect_711.wordsapp.security;

import edu.architect_711.wordsapp.model.dto.AccountDetails;
import edu.architect_711.wordsapp.model.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationExtractor {

    /**
     * Retrieves {@link AccountDetails} from the {@link SecurityContextHolder}
     * @return account details from the security context
     */
    public static AccountDetails getAccountDetails() {
        return (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Retrieves authenticated user object {@link edu.architect_711.wordsapp.model.entity.Account}
     * from the {@link SecurityContextHolder}
     * @return the account entity
     */
    public static Account getAccount() {
        return getAccountDetails().getAccount();
    }
}
