package edu.architect_711.wordsapp.security;

import edu.architect_711.wordsapp.model.dto.account.AccountDetails;
import edu.architect_711.wordsapp.model.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUserExtractor {

    /**
     * Retrieves {@link AccountDetails} from the {@link SecurityContextHolder}.
     * <p>
     * Take care that the context wasn't cleared before otherwise you get the
     * {@link NullPointerException}
     *
     * @return account details from the security context
     */
    public static AccountDetails getAccountDetails() {
        return (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Retrieves authenticated user object {@link edu.architect_711.wordsapp.model.entity.Account}
     * from the {@link SecurityContextHolder}
     * <p>
     * Take care that the context wasn't cleared before otherwise you get the
     * {@link NullPointerException}
     *
     * @return the account entity
     */
    public static Account getAccount() {
        return getAccountDetails().getAccount();
    }
}
