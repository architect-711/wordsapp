package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.DataNotFoundResponse;
import edu.architect_711.wordsapp.model.dto.word.SaveWordRequest;
import edu.architect_711.wordsapp.model.dto.word.UpdateWordRequest;
import edu.architect_711.wordsapp.model.dto.word.WordDto;
import edu.architect_711.wordsapp.service.word.WordService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {
    private final WordService wordService;

    @GetMapping("/groups/{groupId}")
    public List<WordDto> get(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @PathVariable("groupId") Long groupId) {
        return wordService.get(page, size, groupId);
    }

    @PostMapping("/groups/{groupId}")
    public WordDto save(@RequestBody SaveWordRequest saveWordRequest, @PathVariable("groupId") Long groupId) {
        return wordService.save(saveWordRequest, groupId);
    }

    @GetMapping("/{wordId}")
    public Object get(@PathVariable("wordId") Long wordId) {
        var found = wordService.get(wordId); 

        return found.isPresent() ? found : new DataNotFoundResponse("No word found with id: " + wordId);
    }

    @PutMapping
    public WordDto update(@RequestBody UpdateWordRequest updateWordRequest) {
        return wordService.update(updateWordRequest);
    }

    @DeleteMapping("/groups/{groupId}/{wordId}")
    public void delete(@PathVariable("groupId") Long groupId, @PathVariable("wordId") Long wordId) {
        wordService.delete(wordId, groupId);
    }
}
