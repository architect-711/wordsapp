package edu.architect_711.wordsapp.service.group;

import edu.architect_711.wordsapp.model.dto.GroupDto;
import edu.architect_711.wordsapp.model.dto.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.UpdateGroupDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

public interface GroupService {

    /**
     * Save new group based on the authenticated user
     * @param saveGroupDto the group to be saved
     * @return saved group
     */
    GroupDto save(@Valid SaveGroupDto saveGroupDto);

    /**
     * Find all groups, which belong to the single user
     * @return found groups
     */
    List<GroupDto> get();

    /**
     * Update a group info
     * @param updateGroupDto new group info
     * @return updated group 
     * @throws EntityNotFoundException if the group cannot be found by passed `id`
     */
    GroupDto update(@Valid UpdateGroupDto updateGroupDto) throws EntityNotFoundException;


    // TODO: after the word has been added implement the mechanism of their deletion when the group gets deleted
    /**
     * Delete group by its id. Ignores a group absence
     * @param id group id
     */
    void delete(Long id);
}
