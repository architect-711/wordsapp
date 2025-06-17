package edu.architect_711.wordsapp.model.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class SaveGroupDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
