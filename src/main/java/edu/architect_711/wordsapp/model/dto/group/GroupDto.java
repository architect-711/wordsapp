package edu.architect_711.wordsapp.model.dto.group;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Data
@Builder
public class GroupDto {
    @NotNull @Min(0)
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private LocalDateTime created;

    @NotBlank
    private String description;

    @NotNull @Min(0)
    private Long ownerId;
}
