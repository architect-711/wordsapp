package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import edu.architect_711.wordsapp.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public List<GroupDto> get() {
        return groupService.get();
    }

    @PostMapping
    public GroupDto save(@RequestBody SaveGroupDto groupDto) {
        return groupService.save(groupDto);
    }

    @PutMapping
    public GroupDto update(@RequestBody UpdateGroupDto updateGroupDto) {
        return groupService.update(updateGroupDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }
}
