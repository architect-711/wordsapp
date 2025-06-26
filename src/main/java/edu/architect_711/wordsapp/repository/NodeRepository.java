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
    List<Node> findAllByWordId(Long wordId);

    List<Node> findAllByGroupId(Long groupId, Pageable pageable);

    List<Node> findAllByGroupId(Long groupId);

    List<Node> findAllByWordIdAndGroupId(Long wordId, Long groupId);

    Optional<Node> findByGroupId(Long groupId);

    boolean existsByWordIdAndGroupId(Long id, Long groupId);

    @Query(nativeQuery = true, value = """
            select * from
                node
            where
                word_id = :wordId
            limit 1;
            """)
    Optional<Node> findByWordId(@Param("wordId") Long wordId);

    @Modifying
    @Query(nativeQuery = true, value = """
            select exists(
                select count(1) from
                    node
                where
                    word_id = :wordId
                );
                """)
    boolean hasWordReferencesById(@Param("wordId") Long wordId);

    boolean existsByWordId(Long wordId);

    @Transactional // saves from "nothing returned by the query" exception
    @Query(nativeQuery = true, value = """
            delete from
                node
            where
                word_id = :wordId;
            """)
    void deleteByWordId(@Param("wordId") Long wordId);

    default Node safeFindByWordId(Long wordId) {
        return findByWordId(wordId)
                .orElseThrow(() -> new EntityNotFoundException("Word node wasn't found by id: " + wordId));
    }

}
