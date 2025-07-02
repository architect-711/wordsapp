package edu.architect_711.wordsapp.model.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateAccountRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String username;
}
