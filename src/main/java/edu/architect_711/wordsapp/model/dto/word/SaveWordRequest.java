package edu.architect_711.wordsapp.model.dto.word;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SaveWordRequest {
    @NotBlank
    private String title;

    @NotNull
    @Min(0)
    private Long languageId;

    private String definition;

    private String description;

    private List<String> transcriptions;

    private List<String> translations;
}
