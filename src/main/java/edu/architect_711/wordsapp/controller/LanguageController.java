package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.language.LanguageDto;
import edu.architect_711.wordsapp.service.language.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name = "Language", description = "Endpoints for language management")
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @Operation(summary = "Get all languages")
    @ApiResponse(
            responseCode = "200",
            description = "Found languages"
    )
    @SecurityRequirements
    @GetMapping(consumes = "*/*", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LanguageDto>> get() {
        return ResponseEntity.ok(languageService.get());
    }

}
