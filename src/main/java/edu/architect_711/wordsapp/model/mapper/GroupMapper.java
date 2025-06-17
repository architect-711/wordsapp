package edu.architect_711.wordsapp.model.mapper;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.entity.Group;

import java.util.List;

public class GroupMapper {
    public static Group toEntity(SaveGroupDto groupDto, Account account) {
        return new Group(
                groupDto.getTitle(),
                groupDto.getDescription(),
                account
        );
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

    public static UpdateGroupDto toUpdateDto(Group group) {
        return new UpdateGroupDto(
                group.getId(),
                group.getTitle(),
                group.getDescription()
        );
    }

    public static List<GroupDto> toDto(List<Group> groups) {
        return groups
                .stream()
                .map(GroupMapper::toDto)
                .toList();
    }
}
