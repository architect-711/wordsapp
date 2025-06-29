package edu.architect_711.wordsapp.model.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Used for returning the base64 token after
 * a successful authentication
 * <p>
 * Example value: {@code PHN0cmluZz46PHN0cmluZz4=}
 */
@AllArgsConstructor
@Getter
@Setter
public class AuthBaseTokenResponse {
    private String token;
}
