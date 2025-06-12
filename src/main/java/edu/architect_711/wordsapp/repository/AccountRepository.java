package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);



    default Account safeFindByUsername(String username) throws UsernameNotFoundException {
        return this.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
