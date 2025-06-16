package edu.architect_711.wordsapp.service.group;

import edu.architect_711.wordsapp.model.dto.GroupDto;
import edu.architect_711.wordsapp.model.dto.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.UpdateGroupDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.entity.Group;
import edu.architect_711.wordsapp.model.mapper.GroupMapper;
import edu.architect_711.wordsapp.repository.GroupRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toEntity;
import static edu.architect_711.wordsapp.security.AuthenticationExtractor.getAccount;

@Service
@RequiredArgsConstructor
@Validated
public class DefaultGroupService implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    public GroupDto save(@Valid SaveGroupDto saveGroupDto) {
        Account account = getAccount();
        Group group = groupRepository.save(toEntity(saveGroupDto, account));

        return toDto(group);
    }

    @Override
    public List<GroupDto> get() {
        return groupRepository
            .findAll(getAccount().getId())
            .stream()
            .map(GroupMapper::toDto)
            .toList();
    }

    @Override
    public GroupDto update(@Valid UpdateGroupDto updateGroupDto) {
        Group group = groupRepository.safeFindById(updateGroupDto.getId());

        checkGroupId(group.getAccount().getId());

        group.setTitle(updateGroupDto.getTitle());
        group.setDescription(updateGroupDto.getDescription());
        
        return toDto(groupRepository.save(group));
    }

    @Override
    public void delete(Long id) {
        var group = groupRepository.safeFindById(id);
        checkGroupId(group.getAccount().getId());

        groupRepository.deleteById(id);
    }

    private void checkGroupId(long groupAccountId) {
        var account = getAccount();

        if (!Objects.equals(groupAccountId, account.getId()))
            throw new AccessDeniedException("You can't delete another user group!");
    }

}
