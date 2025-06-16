package edu.architect_711.wordsapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity @Table(name = "account_group")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "created")
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Account account;

    public Group(String title, LocalDateTime created, String description, Account account) {
        this.title = title;
        this.created = created;
        this.description = description;
        this.account = account;
    }
}
