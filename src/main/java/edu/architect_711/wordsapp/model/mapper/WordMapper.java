package edu.architect_711.wordsapp.model.mapper;

import edu.architect_711.wordsapp.model.dto.word.SaveWordRequest;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import edu.architect_711.wordsapp.model.entity.Language;
import edu.architect_711.wordsapp.model.entity.Word;

import java.util.List;

public class WordMapper {

    public static WordDto toDto(Word word) {
        return WordDto.builder()
                .id(word.getId())
                .title(word.getTitle())
                .languageId(word.getLanguage().getId())
                .definition(word.getDefinition())
                .description(word.getDescription())
                .transcriptions(word.getTranscriptions())
                .translations(word.getTranslations())
                .created(word.getCreated())
                .build();
    }

    public static Word toEntity(SaveWordRequest word, Language language) {
        return Word.builder()
                .title(word.getTitle())
                .language(language)
                .definition(word.getDefinition())
                .description(word.getDescription())
                .transcriptions(word.getTranscriptions().toArray(new String[0]))
                .translations(word.getTranslations().toArray(new String[0]))
                .build();
    }

    public static Word toEntity(WordDto word, Language language) {
        return Word.builder()
                .title(word.getTitle())
                .language(language)
                .definition(word.getDefinition())
                .description(word.getDescription())
                .transcriptions(word.getTranscriptions().toArray(new String[0]))
                .translations(word.getTranslations().toArray(new String[0]))
                .build();
    }

    public static List<WordDto> toDto(List<Word> words) {
        return words.stream().map(WordMapper::toDto).toList();
    }

    public static SaveWordRequest toSaveRequest(Word word) {
        return SaveWordRequest.builder()
                .title(word.getTitle())
                .languageId(word.getLanguage().getId())
                .definition(word.getDefinition())
                .description(word.getDescription())
                .translations(word.getTranslations())
                .transcriptions(word.getTranscriptions())
                .build();
    }

}
