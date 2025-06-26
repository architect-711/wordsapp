package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.model.dto.language.LanguageDto;
import edu.architect_711.wordsapp.service.language.LanguageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LanguageServiceIntegrationTest {
    @Autowired
    private LanguageService languageService;

    @Test
    public void should_ok__find_all() {
        List<LanguageDto> languageDtos = languageService.get();

        assertNotNull(languageDtos);
        assertFalse(languageDtos.isEmpty());
    }
}
