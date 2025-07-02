package edu.architect_711.wordsapp.security.utils;

import edu.architect_711.wordsapp.model.dto.account.AccountDetails;
import edu.architect_711.wordsapp.model.entity.Account;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuthenticationExtractor {

    public static AccountDetails getAccountDetails() {
        return (AccountDetails) getAuthentication().getPrincipal();
    }

    public static Account getAccount() {
        return getAccountDetails().getAccount();
    }

    public static Account safeGetAccount() {
        return Optional
                .ofNullable(getAccount())
                .orElseThrow(() -> new EntityNotFoundException("The account extracted from the Authentication is null!"));
    }

    public static SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    public static void clearContext() {
        SecurityContextHolder.clearContext();
    }

    public static Authentication getAuthentication() {
        return getContext().getAuthentication();
    }

    public static void setAuthentication(Authentication authentication) {
        getContext().setAuthentication(authentication);
    }
}
