package edu.architect_711.wordsapp.model.mapper;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.entity.Group;

import java.util.List;
import java.util.Set;

public class GroupMapper {
    /**
     * Maps save request to entity, remember that it leaves related words as empty
     * set!!!
     *
     * @param groupDto dto to be mapper
     * @param account  owner
     * @return mapped entity
     */
    public static Group toEntity(SaveGroupDto groupDto, Account account) {
        return new Group(
                groupDto.getTitle(),
                groupDto.getDescription(),
                account,
                Set.of());
    }

    public static GroupDto toDto(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .title(group.getTitle())
                .created(group.getCreated())
                .description(group.getDescription())
                .ownerId(group.getAccount().getId())
                .build();
    }

    public static SaveGroupDto toSaveGroupDto(Group group) {
        return new SaveGroupDto(group.getTitle(), group.getDescription());
    }

    public static List<GroupDto> toDto(List<Group> groups) {
        return groups
                .stream()
                .map(GroupMapper::toDto)
                .toList();
    }
}
