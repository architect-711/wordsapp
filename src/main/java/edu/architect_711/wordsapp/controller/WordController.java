package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.word.SaveWordRequest;
import edu.architect_711.wordsapp.model.dto.word.UpdateWordRequest;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import edu.architect_711.wordsapp.service.word.WordService;
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

@Tag(name = "Word", description = "Endpoints for managing words")
@SecurityRequirement(name = "BasicAuth")
@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @Operation(summary = "Get all words paginated from a group")
    @ApiResponse(
            responseCode = "200",
            description = "Words found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = WordDto.class)))
    )
    @GetMapping(value = "/groups/{groupId}", consumes = "*/*", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WordDto>> get(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable("groupId") Long groupId
    ) {
        return ResponseEntity.ok(wordService.get(page, size, groupId));
    }

    @Operation(summary = "Save new word to group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Word saved",
                    content = @Content(schema = @Schema(implementation = WordDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Word already exists"
            )
    })
    @PostMapping(value = "/groups/{groupId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<WordDto> save(@RequestBody SaveWordRequest saveWordRequest, @PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(wordService.save(saveWordRequest, groupId));
    }

    @Operation(summary = "Get word by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Word found",
                    content = @Content(schema = @Schema(implementation = WordDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Word not found"
            )
    })
    @GetMapping(value = "/{wordId}", consumes = "*/*", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<WordDto> get(@PathVariable("wordId") Long wordId) {
        var found = wordService.get(wordId);

        return found.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update word")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Word updated",
                    content = @Content(schema = @Schema(implementation = WordDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Word not found"
            )
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<WordDto> update(@RequestBody UpdateWordRequest updateWordRequest) {
        return ResponseEntity.ok(wordService.update(updateWordRequest));
    }

    @Operation(summary = "Delete word from group")
    @ApiResponse(
            responseCode = "200",
            description = "Word deleted or ignored"
    )
    @DeleteMapping("/groups/{groupId}/{wordId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("groupId") Long groupId, @PathVariable("wordId") Long wordId) {
        wordService.delete(wordId, groupId);
    }
}
