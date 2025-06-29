package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Node;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findAllByWordId(Long wordId);

    List<Node> findAllByGroupId(Long groupId, Pageable pageable);

    List<Node> findAllByGroupId(Long groupId);

    List<Node> findAllByWordIdAndGroupId(Long wordId, Long groupId);

    Optional<Node> findByGroupId(Long groupId);

    boolean existsByWordIdAndGroupId(Long id, Long groupId);

    boolean existsByWordId(Long wordId);

    Optional<Node> findByWordId(Long wordId);

    default Node safeFindByWordId(Long wordId) {
        return findByWordId(wordId)
                .orElseThrow(() -> new EntityNotFoundException("Word node wasn't found by id: " + wordId));
    }

}
