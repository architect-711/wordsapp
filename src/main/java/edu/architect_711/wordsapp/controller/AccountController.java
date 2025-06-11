package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import edu.architect_711.wordsapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable Long id) {
        return accountService.get(id);
    }

    @PostMapping
    public AccountDto save(@RequestBody SaveAccountDto accountDto) {
        return accountService.save(accountDto);
    }

    // TODO change password

    @PutMapping
    public AccountDto update(@RequestBody AccountDto accountDto) {
        return accountService.update(accountDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        accountService.delete(id);
    }

}
