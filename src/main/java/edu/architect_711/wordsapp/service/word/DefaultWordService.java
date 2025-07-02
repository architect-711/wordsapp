package edu.architect_711.wordsapp.service.word;

import edu.architect_711.wordsapp.model.dto.word.SaveWordRequest;
import edu.architect_711.wordsapp.model.dto.word.UpdateWordRequest;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import edu.architect_711.wordsapp.model.entity.Node;
import edu.architect_711.wordsapp.model.entity.Word;
import edu.architect_711.wordsapp.repository.GroupRepository;
import edu.architect_711.wordsapp.repository.LanguageRepository;
import edu.architect_711.wordsapp.repository.NodeRepository;
import edu.architect_711.wordsapp.repository.WordRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.architect_711.wordsapp.model.mapper.WordMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.WordMapper.toEntity;
import static edu.architect_711.wordsapp.security.utils.AccessChecker.checkGroupAccess;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class DefaultWordService implements WordService {
    private final WordRepository wordRepository;
    private final NodeRepository nodeRepository;
    private final LanguageRepository languageRepository;
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public WordDto save(@Valid SaveWordRequest saveWordRequest, Long groupId) {
        var group = groupRepository.safeFindById(groupId);

        checkGroupAccess(group.getAccount().getId());

        var language = languageRepository.safeFindById(saveWordRequest.getLanguageId());

        Word word = wordRepository
                .findByTitle(saveWordRequest.getTitle())
                .orElseGet(() -> wordRepository.save(toEntity(saveWordRequest, language)));

        if (!nodeRepository.existsByWordIdAndGroupId(word.getId(), groupId))
            nodeRepository.save(new Node(group, word));

        return toDto(word);
    }

    @Override
    public List<WordDto> get(int page, int size, Long groupId) {
        var wordNodes = nodeRepository.findAllByGroupId(groupId, PageRequest.of(page, size));

        if (wordNodes.isEmpty())
            return List.of();

        var res = new ArrayList<WordDto>(wordNodes.size());

        for (Node node : wordNodes) {
            var found = wordRepository.safeFindById(node.getWord().getId());

            res.add(toDto(found));
        }

        return res;
    }

    @Override
    public Optional<WordDto> get(Long wordId) {
        var node = nodeRepository.findByWordId(wordId);

        if (node.isEmpty())
            return Optional.empty();

        var group = groupRepository.safeFindById(node.get().getGroup().getId());

        checkGroupAccess(group.getAccount().getId());

        var word = wordRepository.safeFindById(wordId);

        return Optional.of(toDto(word));
    }

    // TODO @Transactional breaks everything even if specify it also in the controller, but it must be
    @Override
    public void delete(Long wordId, Long groupId) {
        var nodes = nodeRepository.findAllByWordIdAndGroupId(wordId, groupId);

        if (nodes.isEmpty())
            return;

        var group = groupRepository.safeFindById(groupId);

        checkGroupAccess(group.getAccount().getId());

        nodeRepository.deleteAllById(nodes.stream().map(Node::getId).toList());

        // if there's no any groups referencing to this word, then delete it
        if (!nodeRepository.existsByWordId(wordId))
            delete(wordId);
    }

    @Override
    public void delete(Long wordId) {
        wordRepository.deleteById(wordId);
    }

    @Override
    public WordDto update(@Valid UpdateWordRequest updateWord) {
        var word = wordRepository.safeFindById(updateWord.getId());
        var lang = languageRepository.safeFindById(updateWord.getLanguageId());

        word.setTitle(updateWord.getTitle());
        word.setLanguage(lang);
        word.setDefinition(updateWord.getDefinition());
        word.setDescription(updateWord.getDescription());
        word.setTranslations(updateWord.getTranslations().toArray(new String[0]));
        word.setTranscriptions(updateWord.getTranscriptions().toArray(new String[0]));

        return toDto(wordRepository.save(word));

    }

}
