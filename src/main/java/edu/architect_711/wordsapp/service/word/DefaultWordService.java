package edu.architect_711.wordsapp.service.word;

import edu.architect_711.wordsapp.exception.NodePointsToNothingException;
import edu.architect_711.wordsapp.exception.WordNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static edu.architect_711.wordsapp.model.mapper.WordMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.WordMapper.toEntity;
import static edu.architect_711.wordsapp.security.utils.CheckAccess.checkGroupAccess;

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

        Optional<Word> found = wordRepository.findByTitle(saveWordRequest.getTitle());
        Word word = found.isEmpty()
                ? wordRepository.save(toEntity(saveWordRequest, language))
                : found.get();

        if (!nodeRepository.existsByWordIdAndGroupId(word.getId(), groupId)) {
            var nodeMap = new Node(group, word);

            nodeRepository.save(nodeMap);
        }

        return toDto(word);
    }

    @Override
    public List<WordDto> get(int page, int size, Long groupId) {
        var wordNodes = nodeRepository.findAllByGroupId(groupId, PageRequest.of(page, size));
        var res = new ArrayList<WordDto>(wordNodes.size());

        for (Node node : wordNodes) {
            var found = wordRepository.findById(node.getWord().getId());

            // This should never happen
            if (found.isEmpty())
                stateFail("This should never happen. For some reason the node's word_id points to the void.");

            res.add(toDto(found.get()));
        }

        return res;
    }

    @Override
    public Optional<WordDto> get(Long wordId) {
        var node = nodeRepository.findByWordId(wordId);

        if (node.isEmpty())
            return Optional.empty();

        var group = groupRepository.findById(node.get().getGroup().getId());

        // should never happen
        if (group.isEmpty())
            stateFail("Despite the node exist, the group wasn't found");

        checkGroupAccess(group.get().getAccount().getId());

        var word = wordRepository.findById(wordId);

        // should never happen
        if (word.isEmpty())
            stateFail("For some reason the word does not exist, although the node exists! This should never happen.");

        return Optional.of(toDto(word.get()));
    }

    @Transactional
    @Override
    public void delete(Long wordId, Long groupId) {
        // findAll because for the case, when data was saved not properly
        var nodes = nodeRepository.findAllByWordIdAndGroupId(wordId, groupId);

        if (nodes.isEmpty())
            return;

        var group = groupRepository.findById(groupId).orElseGet(() -> null);

        // should never happen
        if (group == null) {
            stateFail("This should never happen. The node points to the fabulous group!");
            return;
        }

        checkGroupAccess(group.getAccount().getId());

        nodeRepository.deleteAllById(nodes.stream().map(Node::getId).toList());

        // if there's no any groups refering to this word, then delete it
        if (!nodeRepository.existsByWordId(wordId)) {
            delete(wordId);
        }
    }

    @Override
    public void delete(Long wordId) {
        wordRepository.deleteById(wordId);
    }

    @Override
    public WordDto update(@Valid UpdateWordRequest updateWord) {
        var word = wordRepository.findById(updateWord.getId()).orElseThrow(() -> new WordNotFoundException("Word was not found with id: " + updateWord.getId()));
        var lang = languageRepository.safeFindById(updateWord.getLanguageId());

        word.setTitle(updateWord.getTitle());
        word.setLanguage(lang);
        word.setDefinition(updateWord.getDefinition());
        word.setDescription(updateWord.getDescription());
        word.setTranslations(updateWord.getTranslations().toArray(new String[0]));
        word.setTranscriptions(updateWord.getTranscriptions().toArray(new String[0]));

        return toDto(wordRepository.save(word));

    }

    private void stateFail(String message) throws NodePointsToNothingException {
        log.error(message);
        throw new NodePointsToNothingException(message);
    }

}
