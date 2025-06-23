package edu.architect_711.wordsapp.exception;

import org.springframework.security.access.AccessDeniedException;

public class UnauthorizedGroupModifyAttemptException extends AccessDeniedException {
    public UnauthorizedGroupModifyAttemptException(String msg) {
        super(msg);
    }

    public UnauthorizedGroupModifyAttemptException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
