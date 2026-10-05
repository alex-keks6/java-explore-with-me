package ru.practicum.explore.main.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.explore.main.enums.ParticipationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "participations")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User register;
    @Column(name = "status", nullable = false)
    private ParticipationStatus status;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Participation that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public final int hashCode() {
        return Participation.class.hashCode();
    }
}