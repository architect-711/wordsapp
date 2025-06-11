package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.model.dto.LanguageDto;
import edu.architect_711.wordsapp.service.language.LanguageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
