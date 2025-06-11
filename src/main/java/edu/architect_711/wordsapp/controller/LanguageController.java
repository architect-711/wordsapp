package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.LanguageDto;
import edu.architect_711.wordsapp.service.language.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping
    public List<LanguageDto> get() {
        return languageService.get();
    }

}
