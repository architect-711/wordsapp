package edu.architect_711.wordsapp.utils;

import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.clearContext;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAuthentication;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestUtils {

    public static void safeCleanAuth() {
        clearContext();
        assertNull(getAuthentication());
    }
}
