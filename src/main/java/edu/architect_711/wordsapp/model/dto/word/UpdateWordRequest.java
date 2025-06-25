package edu.architect_711.wordsapp.model.dto.word;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateWordRequest {

    @NotNull @Min(0)
    private Long id;

    @NotBlank
    private String title;

    @NotNull @Min(0)
    private Long languageId;

    private String definition;

    private String description;

    private List<String> translations;

    private List<String> transcriptions;
    
}
