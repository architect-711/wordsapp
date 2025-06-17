package edu.architect_711.wordsapp.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Generator {
    public static String generate(String username, String password) {
        String credentials = username + ":" + password;

        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
