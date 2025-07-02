package edu.architect_711.wordsapp.model.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
