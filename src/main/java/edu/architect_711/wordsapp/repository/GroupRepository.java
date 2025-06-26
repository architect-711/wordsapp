package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Group;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query(
        nativeQuery = true,
        value = """
                select * from
                    account_group
                where
                    account_group.owner_id = :userId 
                """ 
    )
    List<Group> findAll(@Param("userId") Long userId); 

    @Query(
        nativeQuery = true,
        value = """
                select * from
                    account_group
                where
                    title = :title
                limit 1; 
                """
    )
    Optional<Group> findByTitle(@Param("title") String title);


    // "safe" methods
    default Group safeFindById(Long id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Group with such id not found: " + id));
    }

}
