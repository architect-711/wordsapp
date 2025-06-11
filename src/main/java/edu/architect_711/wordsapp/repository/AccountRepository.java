package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
