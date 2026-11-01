package net.engineeringdigest.journalApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.engineeringdigest.journalApp.enums.Sentiment;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_journal_user_updated", columnList = "user_id, updatedAt"),
        @Index(name = "idx_journal_user_state", columnList = "user_id, archived, favorite")
})
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 10000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean archived;

    @Column(nullable = false)
    private boolean favorite;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;
}
