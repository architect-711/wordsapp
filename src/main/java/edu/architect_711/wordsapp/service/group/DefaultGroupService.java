package edu.architect_711.wordsapp.service.group;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.entity.Group;
import edu.architect_711.wordsapp.repository.GroupRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toDto;
import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toEntity;
import static edu.architect_711.wordsapp.security.utils.AuthenticationExtractor.getAccount;
import static edu.architect_711.wordsapp.security.utils.CheckAccess.checkGroupAccess;

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

    @Override
    public void delete(Long id) {
        var group = groupRepository.safeFindById(id);
        checkGroupAccess(group.getAccount().getId());

        groupRepository.deleteById(id);
    }

}
