package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.LoginRequest;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import edu.architect_711.wordsapp.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public AccountDto getById() {
        return accountService.get();
    }

    @PostMapping("/login64")
    public String login64(@RequestBody LoginRequest loginRequest) {
        return accountService.login64(loginRequest);
    }

    @PostMapping
    public AccountDto save(@RequestBody SaveAccountDto accountDto) {
        return accountService.save(accountDto);
    }

    // TODO change password, and verification by email

    @PutMapping
    public AccountDto update(@RequestBody AccountDto accountDto) {
        return accountService.update(accountDto);
    }

    @DeleteMapping
    public void delete() {
        accountService.delete();
    }

}
