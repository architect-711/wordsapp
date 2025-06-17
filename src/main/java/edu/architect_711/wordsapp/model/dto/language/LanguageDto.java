package edu.architect_711.wordsapp.model.dto.language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LanguageDto {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
}
