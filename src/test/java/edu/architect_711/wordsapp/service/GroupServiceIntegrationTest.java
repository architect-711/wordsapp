package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.model.dto.AccountDetails;
import edu.architect_711.wordsapp.model.dto.GroupDto;
import edu.architect_711.wordsapp.model.dto.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.UpdateGroupDto;
import edu.architect_711.wordsapp.model.entity.Account;
import edu.architect_711.wordsapp.repository.AccountRepository;
import edu.architect_711.wordsapp.repository.GroupRepository;
import edu.architect_711.wordsapp.service.group.GroupService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toUpdateDto;
import static edu.architect_711.wordsapp.security.AuthenticationExtractor.getAccount;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupServiceIntegrationTest {
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final SaveGroupDto UNEXISTING_GROUP_DTO = genSaveDto();
    private GroupDto savedBuff = null;

    public SaveGroupDto genSaveDto() {
        return new SaveGroupDto("test_" + LocalDateTime.now(), "some description");
    }

    private void authenticate(Account account) {
        assertNotNull(account);

        var accountDetails = new AccountDetails(account);

        // Note, that it works only when calling the service, because it doesn't care
        // about unencrypted password
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(accountDetails,
                accountDetails.getPassword(), List.of());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @BeforeAll
    public void setup() {
        authenticate(accountRepository.safeFindFirst());
    }



    @Order(1)
    @Test
    public void should_ok__save() {
        GroupDto saved = assertDoesNotThrow(() -> groupService.save(UNEXISTING_GROUP_DTO));
        savedBuff = saved;

        assertTrue(groupRepository.findById(saved.getId()).isPresent());

        assertNotNull(saved);
        assertEquals(saved.getTitle(), UNEXISTING_GROUP_DTO.getTitle());
        assertEquals(saved.getDescription(), UNEXISTING_GROUP_DTO.getDescription());
    }
    @Order(2)
    @Test
    public void should_fail__save__illegal_arguments() {
        var invalid = new SaveGroupDto("         \n ", null);

        assertThrows(ConstraintViolationException.class, () -> groupService.save(invalid));
    }



    @Order(3)
    @Test
    public void should_ok__get_all() {
        final int LIMIT = 10;
        if (groupRepository.count() < 10) {
            for (byte i = 0; i < LIMIT; i++)
                groupService.save(genSaveDto());
        }

        var found = assertDoesNotThrow(() -> groupService.get());

        assertTrue(found.size() >= LIMIT);

        found.forEach(f -> assertEquals(f.getOwnerId(), getAccount().getId()));
    }



    // TODO fix update method in the account tests and service
    @Order(4)
    @Test
    public void should_ok__update() {
        var update = new UpdateGroupDto(savedBuff.getId(), "new_test" + LocalDateTime.now(), "new description !");

        var updated = assertDoesNotThrow(() -> groupService.update(update));

        assertNotNull(updated);
        assertEquals(updated.getTitle(), update.getTitle());
        assertEquals(updated.getDescription(), update.getDescription());
    }

    @Order(5)
    @Test
    public void should_fail__update__fabulous_id() {
        final Long REALLY_FABULOUS_ID = 999999L;
        var update = new UpdateGroupDto(REALLY_FABULOUS_ID, "this should not be saved in the database",
                "if it did, checkout the test files");

        assertThrows(EntityNotFoundException.class, () -> groupService.update(update));
    }
    @Order(6)
    @Test
    public void should_fail__update__illegal_arguments() {
        var update = new UpdateGroupDto(-1L, null, "                   \0  ");

        assertThrows(ConstraintViolationException.class, () -> groupService.update(update));
    }
    @Order(7)
    @Test
    public void should_fail__update__third_party_access() {
        // TODO update another user group
        var account = accountRepository.findAll(PageRequest.of(1, 1)).getContent().getFirst(); // find second user
        var backupAuth = SecurityContextHolder.getContext().getAuthentication(); // i hope that will not be changed

        authenticate(account); // login as another user

        var group = groupRepository.safeFindById(savedBuff.getId()); // group of original user

        assertThrows(AccessDeniedException.class, () -> groupService.update(toUpdateDto(group))); // can't access

        SecurityContextHolder.getContext().setAuthentication(backupAuth); // get things back
    }



    @Order(8)
    @Test
    public void should_fail__delete__third_party_access() {
        var backupAuth = SecurityContextHolder.getContext().getAuthentication();
        var anotherUser = accountRepository.findAll(PageRequest.of(1, 1)).getContent().getFirst();
        var group = groupRepository.safeFindById(savedBuff.getId()); // TODO: but why safe find first???

        authenticate(anotherUser);

        assertThrows(AccessDeniedException.class, () -> groupService.delete(group.getId()));

        SecurityContextHolder.getContext().setAuthentication(backupAuth);
    }
    @Order(9)
    @Test
    public void should_ok__delete() {
        assertDoesNotThrow(() -> groupService.delete(savedBuff.getId()));

        assertTrue(groupRepository.findById(savedBuff.getId()).isEmpty());
    }



    @AfterAll
    public void clean() { // TODO don't forget to remove
        groupRepository.deleteAll();
    }
}
