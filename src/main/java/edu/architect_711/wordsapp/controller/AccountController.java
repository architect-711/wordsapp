package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.account.AccountDto;
import edu.architect_711.wordsapp.model.dto.account.AccountLoginRequest;
import edu.architect_711.wordsapp.model.dto.account.SaveAccountDto;
import edu.architect_711.wordsapp.service.account.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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

    @SecurityRequirements
    @PostMapping("/login64")
    public String login64(@RequestBody AccountLoginRequest accountLoginRequest) {
        return accountService.login64(accountLoginRequest);
    }

    @SecurityRequirements
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
