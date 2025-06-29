package edu.architect_711.wordsapp.service.group;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.entity.Group;
import edu.architect_711.wordsapp.repository.GroupRepository;
import edu.architect_711.wordsapp.repository.NodeRepository;
import edu.architect_711.wordsapp.service.word.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toEntity;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAccount;
import static edu.architect_711.wordsapp.security.utils.AccessChecker.checkGroupAccess;

@Service
@RequiredArgsConstructor
@Validated
public class DefaultGroupService implements GroupService {
    private final GroupRepository groupRepository;
    private final NodeRepository nodeRepository;

    private final WordService wordService;

    @Override
    public GroupDto save(@Valid SaveGroupDto saveGroupDto) {
        Account account = getAccount();
        Group group = groupRepository.save(toEntity(saveGroupDto, account));

        return toDto(group);
    }

    @Override
    public List<GroupDto> get() {
        return toDto(groupRepository.findAll(getAccount().getId()));
    }

    @Override
    public GroupDto update(@Valid UpdateGroupDto updateGroupDto) {
        Group group = groupRepository.safeFindById(updateGroupDto.getId());

        checkGroupAccess(group.getAccount().getId());

        group.setTitle(updateGroupDto.getTitle());
        group.setDescription(updateGroupDto.getDescription());

        return toDto(groupRepository.save(group));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        var group = groupRepository.findById(id).orElse(null);

        if (group == null)
            return;

        checkGroupAccess(group.getAccount().getId());

        var nodes = nodeRepository.findAllByGroupId(group.getId());

        if (!nodes.isEmpty())
            nodes.forEach(n -> wordService.delete(n.getWord().getId(), group.getId()));

        groupRepository.deleteById(id);
    }

}