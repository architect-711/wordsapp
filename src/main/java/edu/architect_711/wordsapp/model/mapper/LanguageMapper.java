package edu.architect_711.wordsapp.model.mapper;

import edu.architect_711.wordsapp.model.dto.LanguageDto;
import edu.architect_711.wordsapp.model.entity.Language;

public class LanguageMapper {
    public static Language toEntity(LanguageDto language) {
        return new Language(language.getId(), language.getTitle());
    }

    public static LanguageDto toDto(Language language) {
        return new LanguageDto(language.getId(), language.getTitle());
    }
}
