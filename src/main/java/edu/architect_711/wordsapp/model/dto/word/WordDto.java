package edu.architect_711.wordsapp.model.dto.word;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WordDto {

    @NotNull
    @Min(0)
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    @Min(0)
    private Long languageId;

    private String definition;

    private String description;

    private List<String> translations;

    private List<String> transcriptions;

    @NotNull
    private LocalDateTime created;

}
