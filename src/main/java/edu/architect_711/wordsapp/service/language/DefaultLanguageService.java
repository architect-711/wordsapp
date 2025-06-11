package edu.architect_711.wordsapp.service.language;

import edu.architect_711.wordsapp.model.dto.LanguageDto;
import edu.architect_711.wordsapp.model.mapper.LanguageMapper;
import edu.architect_711.wordsapp.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class DefaultLanguageService implements LanguageService {
    private final LanguageRepository languageRepository;

    @Override
    public List<LanguageDto> get() {
        return languageRepository.findAll().stream().map(LanguageMapper::toDto).toList();
    }
}
