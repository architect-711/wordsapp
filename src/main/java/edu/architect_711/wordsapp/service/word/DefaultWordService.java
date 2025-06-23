package edu.architect_711.wordsapp.service.word;

import edu.architect_711.wordsapp.model.dto.word.SaveWordRequest;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import edu.architect_711.wordsapp.model.entity.Node;
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

        var model = toEntity(saveWordRequest, language);
        var savedWord = wordRepository.save(model);

        var nodeMap = new Node(group, savedWord);
        nodeRepository.save(nodeMap);

        return toDto(savedWord);
    }

    @Override
    public List<WordDto> get(int page, int size, Long groupId) {
        var wordNodes = nodeRepository.findAllByGroupId(groupId, PageRequest.of(page, size));
        var res = new ArrayList<WordDto>(wordNodes.size());

        for (Node node : wordNodes) {
            var found = wordRepository.findById(node.getWord().getId());

            // This should never happen
            if (found.isEmpty())
                handleAlarmWordGhostPointerState(node);

            res.add(toDto(found.get()));
        }

        return res;
    }

    private void handleAlarmWordGhostPointerState(Node node) {
        log.error("This method should never occur to happen! It means, that the node refers to unexisting word");

        nodeRepository.deleteById(node.getId());

        log.error("For some reason the node's word id points to nothing! The node's been deleted.");

        // if there is no any references to this word anymore
        if (nodeRepository.findAllByWordId(node.getWord().getId()).isEmpty()) {
            // then, no refers no word
            wordRepository.deleteById(node.getWord().getId());

            log.warn("Since there is no any points to the word anymore, then removing word...");
        }
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

    private void stateFail(String message) {
        log.error(message);
        throw new IllegalStateException(message);
    }

}
