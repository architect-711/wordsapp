package edu.architect_711.wordsapp.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * Used when a user tries to access in any way the value he has no
 * rights to (like another user group, word, or account at all)
 */
public class UnauthorizedGroupModifyAttemptException extends AccessDeniedException {
    public UnauthorizedGroupModifyAttemptException(String msg) {
        super(msg);
    }
}
