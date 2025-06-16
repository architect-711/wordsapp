package edu.architect_711.wordsapp.model.mapper;

import edu.architect_711.wordsapp.model.dto.GroupDto;
import edu.architect_711.wordsapp.model.dto.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.UpdateGroupDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.entity.Group;

public class GroupMapper {
    public static Group toEntity(SaveGroupDto groupDto, Account account) {
        return new Group(groupDto.getTitle(), null, groupDto.getDescription(), account);
    } 

    public static GroupDto toDto(Group group) {
        return new GroupDto(group.getId(), group.getTitle(), group.getCreated(), group.getDescription(), group.getAccount().getId());
    }

    public static UpdateGroupDto toUpdateDto(Group group) {
        return new UpdateGroupDto(group.getId(), group.getTitle(), group.getDescription());
    }
}
