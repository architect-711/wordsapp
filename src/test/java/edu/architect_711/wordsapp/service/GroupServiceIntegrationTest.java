package edu.architect_711.wordsapp.service;

import edu.architect_711.wordsapp.model.dto.group.GroupDto;
import edu.architect_711.wordsapp.model.dto.group.SaveGroupDto;
import edu.architect_711.wordsapp.model.dto.group.UpdateGroupDto;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static edu.architect_711.wordsapp.Authenticator.authenticate;
import static edu.architect_711.wordsapp.model.mapper.GroupMapper.toUpdateDto;
import static edu.architect_711.wordsapp.security.AuthenticatedUserExtractor.getAccount;
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
    /**
     * <b>NOTE!!!</b> This var MUST never be cleared or reassigned. Treat it as FINAL.
     * It represents saved by the {@link GroupServiceIntegrationTest#should_ok__save()} method value
     */
    private GroupDto savedBuff = null;

    public SaveGroupDto genSaveDto() {
        return new SaveGroupDto("test_" + LocalDateTime.now(), "some description");
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
        var saved = new ArrayList<GroupDto>();

        final int LIMIT = 10;
        if (groupRepository.count() < 10) {
            for (byte i = 0; i < LIMIT; i++)
                saved.add(groupService.save(genSaveDto()));
        }

        var found = assertDoesNotThrow(() -> groupService.get());

        assertTrue(found.size() >= LIMIT);

        found.forEach(f -> assertEquals(f.getOwnerId(), getAccount().getId()));

        saved.forEach(s -> groupRepository.deleteById(s.getId()));
    }



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
        var account = accountRepository.findAll(PageRequest.of(1, 1)).getContent().getFirst(); // find second user
        var backupAuth = SecurityContextHolder.getContext().getAuthentication(); // I hope that will not be changed

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
        var group = groupRepository.safeFindById(savedBuff.getId());

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
}
