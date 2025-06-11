package edu.architect_711.wordsapp.model.mapper;

import edu.architect_711.wordsapp.model.dto.AccountDto;
import edu.architect_711.wordsapp.model.dto.SaveAccountDto;
import edu.architect_711.wordsapp.model.entity.Account;

public class AccountMapper {
    public static AccountDto toDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getUsername(),
                account.getEmail()
        );
    }

    public static Account toEntity(AccountDto accountDto, String password) {
        return new Account(
                accountDto.getId(),
                accountDto.getUsername(),
                password,
                accountDto.getEmail()
        );
    }

    public static Account toEntity(SaveAccountDto dto) {
        Account account = new Account();

        account.setUsername(dto.getUsername());
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword());

        return account;
    }
}
