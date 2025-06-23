package edu.architect_711.wordsapp.utils;

import edu.architect_711.wordsapp.model.dto.account.SaveAccountDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.model.entity.Group;
import edu.architect_711.wordsapp.model.entity.Language;
import edu.architect_711.wordsapp.model.entity.Word;

import java.time.LocalDateTime;
import java.util.Set;

/*
 * Generates @Entities to be persisted for testing
 */
public class TestEntityGenerator {
    public static Account account() {
        return new Account(
                "email_" + LocalDateTime.now(),
                "pass",
                "test_" + LocalDateTime.now());
    }

    public static Word word(Language language) {
        return Word.builder()
                .title("test_" + LocalDateTime.now())
                .language(language).definition("def")
                .description("description")
                .transcriptions(new String[] { "asdf", "asdf" })
                .translations(new String[] { "asdf" })
                .build();
    }

    public static SaveAccountDto saveAccountDto() {
        return new SaveAccountDto(
                "test_" + LocalDateTime.now(),
                "1234",
                LocalDateTime.now() + "_name@domain.tld");
    }

    public static Group group(Account account) {
        return new Group(
                "title_" + LocalDateTime.now(),
                "some description",
                account,
                Set.of());
    }

    public static UpdateGroupDto updateGroupDto(Long id) {
        return new UpdateGroupDto(
                id,
                "new_test" + LocalDateTime.now(),
                "new description!");
    }
}
