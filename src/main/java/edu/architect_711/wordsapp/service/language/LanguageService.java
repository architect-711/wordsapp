package edu.architect_711.wordsapp.service.language;

import edu.architect_711.wordsapp.model.dto.language.LanguageDto;

import java.util.List;

public interface LanguageService {
    /**
     * Find all languages
     * @return all languages
     */
    List<LanguageDto> get();
}
