package edu.architect_711.wordsapp.security.utils;

import edu.architect_711.wordsapp.exception.UnauthorizedGroupModifyAttemptException;

import java.util.Objects;

import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAccount;

public class AccessChecker {
    /**
     * Checks that the group's account id equals to the
     * authenticated user id, i.e. user can modify this group
     *
     * @param groupAccountId the group's owner id to be affected
     */
    public static void checkGroupAccess(long groupAccountId) {
        var account = getAccount();

        if (!Objects.equals(groupAccountId, account.getId()))
            throw new UnauthorizedGroupModifyAttemptException("You can't access another user group!");
    }
}
