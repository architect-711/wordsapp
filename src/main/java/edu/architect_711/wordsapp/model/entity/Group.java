package edu.architect_711.wordsapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "account_group")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "created")
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Account account;

    @ManyToMany(fetch = FetchType.EAGER) // saves from "No session" exception
    @JoinTable(
            name = "node",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private Set<Word> words;

    public Group(String title, String description, Account account, Set<Word> words) {
        this.title = title;
        this.description = description;
        this.account = account;
        this.words = words;
    }
}
