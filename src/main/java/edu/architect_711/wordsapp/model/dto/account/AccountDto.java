package edu.architect_711.wordsapp.model.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object is usually used to return the saved
 * {@link edu.architect_711.wordsapp.model.entity.Account}.
 * <p>
 * The `password` field is absent for the security terms
 * (and why if it's encrypted?)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @NotNull
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
}
