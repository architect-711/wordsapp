package edu.architect_711.wordsapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AccountDto {
    @NotNull
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
}
