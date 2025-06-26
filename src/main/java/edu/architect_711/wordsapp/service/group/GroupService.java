package edu.architect_711.wordsapp.service.group;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

public interface GroupService {

    /**
     * Save new group based on the authenticated user
     * 
     * @param saveGroupDto the group to be saved
     * @return saved group
     */
    GroupDto save(@Valid SaveGroupDto saveGroupDto);

    /**
     * Find all groups, which belong to the single user
     * 
     * @return found groups
     */
    List<GroupDto> get();

    /**
     * Update a group info
     * 
     * @param updateGroupDto new group info
     * @return updated group
     * @throws EntityNotFoundException if the group cannot be found by passed `id`
     */
    GroupDto update(@Valid UpdateGroupDto updateGroupDto) throws EntityNotFoundException;

    /**
     * Delete group by its id. Ignores a group absence.
     * <p>
     * Note! The group deletion leads to lost all words,
     * except for those, which any other group has references to.
     * 
     * @param id group id
     */
    void delete(Long id);
}
