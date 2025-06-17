package edu.architect_711.wordsapp.model.dto.group;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class UpdateGroupDto {
    @NotNull @Min(0)
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}