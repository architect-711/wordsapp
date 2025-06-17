package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.language.LanguageDto;
import edu.architect_711.wordsapp.service.language.LanguageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @SecurityRequirements
    @GetMapping
    public List<LanguageDto> get() {
        return languageService.get();
    }

}
