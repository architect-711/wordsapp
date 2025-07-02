package edu.architect_711.wordsapp.service.language;

import edu.architect_711.wordsapp.model.dto.language.LanguageDto;
import edu.architect_711.wordsapp.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.architect_711.wordsapp.model.mapper.LanguageMapper.toDto;

@Service
@RequiredArgsConstructor
public class DefaultLanguageService implements LanguageService {
    private final LanguageRepository languageRepository;

    @Override
    public List<LanguageDto> get() {
        return toDto(languageRepository.findAll());
    }
}
