package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Account;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    // "safe" methods
    default Account safeFindByUsername(String username) throws UsernameNotFoundException {
        return this.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    default Optional<Account> findFirst() {
        return Optional.ofNullable(findAll(PageRequest.of(0, 1)).getContent().getFirst());
    }

    default Account safeFindFirst() throws EntityNotFoundException {
        Optional<Account> content = findFirst();

        if (content.isEmpty())
            throw new EntityNotFoundException("There MUST be at least one user in the database, but found nothing");

        return content.get();
    }

    /**
     * Find N-th element in the database
     *
     * @param n page, MUST be >= 0
     * @return {@link Optional}
     */
    default Optional<Account> findN(int n) {
        return Optional.ofNullable(findAll(PageRequest.of(n, 1)).getContent().getFirst());
    }

    /**
     * Find N-th element in the database
     *
     * @param n page, MUST be >= 0
     * @return {@link Optional}
     */
    default Account safeFindN(int n) {
        return findN(n).orElseThrow(() -> new EntityNotFoundException("No entity found"));
    }
}
