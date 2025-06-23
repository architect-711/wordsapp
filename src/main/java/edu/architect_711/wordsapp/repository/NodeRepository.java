package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Node;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    @Query(
            nativeQuery = true,
            value = """
                    select * from
                        node 
                    where
                        word_id = :wordId
                    limit 1;
                    """
    )
    Optional<Node> findByWordId(@Param("wordId") Long wordId);

    // // @Query(
    // //     nativeQuery = true,
    // //     value = """
                
    // //             """
    // )
    // TODO expirement, write custom SQL if fails
    List<Node> findAllByGroupId(Long groupId, Pageable pageable);

    @Modifying
    @Transactional // saves from "nothing returned by the query" exception
    @Query(
            nativeQuery = true,
            value = """
                    delete from
                        node 
                    where
                        word_id = :wordId;
                    """
    )
    void deleteByWordId(@Param("wordId") Long wordId);


    default Node safeFindByWordId(Long wordId) {
        return findByWordId(wordId).orElseThrow(() -> new EntityNotFoundException("Word node wasn't found by id: " + wordId));
    }

    List<Node> findAllByWordId(Long wordId);
}
