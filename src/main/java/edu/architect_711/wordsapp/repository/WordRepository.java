package edu.architect_711.wordsapp.repository;

import edu.architect_711.wordsapp.model.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(nativeQuery = true, value = """
            select
                word.id,
                word.title,
                word.language_id,
                word.definition,
                word.description,
                word.translations,
                word.transcriptions,
                word.created
            from
                word
            join
                group_word
            on
                word.id = group_word.word_id
            and
                group_word.group_id = :groupId;
            """)
    List<Word> findAllByGroupId(@Param("groupId") Long groupId);

    Optional<Word> findByTitle(String title);

}
