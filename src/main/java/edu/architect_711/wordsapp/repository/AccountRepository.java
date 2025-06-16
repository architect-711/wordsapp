package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Account;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);


    default Account safeFindByUsername(String username) throws UsernameNotFoundException {
        return this.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    default Account safeFindFirst() throws EntityNotFoundException {
        List<Account> content = findAll(PageRequest.of(0, 1)).getContent();

        if (content.isEmpty())
            throw new EntityNotFoundException("There MUST be at least one user in the databse, but found nothing");

        return content.getFirst();
    }
}
