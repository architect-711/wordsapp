package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Account;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    default Account safeFindByUsername(String username) throws EntityNotFoundException {
        return this.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }
}
