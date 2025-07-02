package edu.architect_711.wordsapp.service.group;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import jakarta.validation.Valid;

import java.util.List;

public interface GroupService {

    /**
     * Save new group and assign to the authenticated user
     *
     * @param saveGroupDto the group to be saved
     * @return saved group
     */
    GroupDto save(@Valid SaveGroupDto saveGroupDto);

    /**
     * Find all authenticated user groups
     *
     * @return found groups
     */
    List<GroupDto> get();

    /**
     * Update a group info
     *
     * @param updateGroupDto new group info
     * @return updated group
     */
    GroupDto update(@Valid UpdateGroupDto updateGroupDto);

    /**
     * Delete group by id. Ignores a group absence.
     * <p>
     * Note! The group deletion leads to lost all words,
     * except for those, which any other group has references to.
     *
     * @param id group id
     */
    void delete(Long id);
}
