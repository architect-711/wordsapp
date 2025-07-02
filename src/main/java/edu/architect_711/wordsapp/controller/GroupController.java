package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import edu.architect_711.wordsapp.service.group.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Group", description = "Endpoints for managing groups")
@SecurityRequirement(name = "BasicAuth")
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "Get a list of account's groups")
    @ApiResponse(
            responseCode = "200",
            description = "Groups found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroupDto.class)))
    )
    @GetMapping(consumes = "*/*", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupDto>> get() {
        return ResponseEntity.ok(groupService.get());
    }

    @Operation(summary = "Save new group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group saved"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Group already exists"
            )
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDto> save(@RequestBody SaveGroupDto groupDto) {
        return ResponseEntity.ok(groupService.save(groupDto));
    }

    @Operation(summary = "Update group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found"
            )
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDto> update(@RequestBody UpdateGroupDto updateGroupDto) {
        return ResponseEntity.ok(groupService.update(updateGroupDto));
    }

    @Operation(summary = "Delete group", description = "Delete a group even if it doesn't exist")
    @ApiResponse(
            responseCode = "200",
            description = "Group deleted or ignored"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }
}
