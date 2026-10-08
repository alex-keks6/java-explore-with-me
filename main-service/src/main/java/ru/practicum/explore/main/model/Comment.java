package ru.practicum.explore.main.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.explore.main.enums.State;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text", nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User commenter;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private State state;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
}
