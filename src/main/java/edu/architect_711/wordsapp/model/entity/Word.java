package edu.architect_711.wordsapp.model.entity;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "word")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "description")
    private String description;

    @Column(name = "definition")
    private String definition;

    @Column(name = "translations")
    @Type(value = StringArrayType.class)
    private String[] translations;

    @Column(name = "transcriptions")
    @Type(value = StringArrayType.class)
    private String[] transcriptions;

    @Column(name = "created", nullable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @ManyToMany(mappedBy = "words")
    private Set<Group> groups;

    public List<String> getTranscriptions() {
        return Arrays.asList(transcriptions);
    }

    public List<String> getTranslations() {
        return Arrays.asList(translations);
    }

}
