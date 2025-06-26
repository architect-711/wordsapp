package edu.architect_711.wordsapp.model.mapper;

import edu.architect_711.wordsapp.model.dto.account.AccountDto;
import edu.architect_711.wordsapp.model.dto.account.SaveAccountDto;
import edu.architect_711.wordsapp.model.entity.Account;

import java.util.Set;

public class AccountMapper {
    public static AccountDto toDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getUsername(),
                account.getEmail());
    }

    /**
     * Usually used in tests when the
     * {@link edu.architect_711.wordsapp.service.account.AccountService#save(SaveAccountDto)}
     * has returned the {@link AccountDto} but we need to somehow set Authentication
     * object, that required the `password` field
     * to be specified. In such case the raw password is usually passed
     * 
     * @param accountDto saved account
     * @param password   might be raw password, use carefully
     * @return mapped account entity
     */
    public static Account toEntity(AccountDto accountDto, String password) {
        return new Account(
                accountDto.getId(),
                accountDto.getUsername(),
                password,
                accountDto.getEmail(),
                Set.of());
    }

    public static Account toEntity(SaveAccountDto dto) {
        Account account = new Account();

        account.setUsername(dto.getUsername());
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword());

        return account;
    }

    public static SaveAccountDto toSaveAccountDto(AccountDto dto, String password) {
        return new SaveAccountDto(dto.getUsername(), password, dto.getEmail());
    }

    public static SaveAccountDto toSaveAccountDto(Account account) {
        return toSaveAccountDto(toDto(account), account.getPassword());
    }
}
