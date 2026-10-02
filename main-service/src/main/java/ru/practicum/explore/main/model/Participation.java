package ru.practicum.explore.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "participations")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Column(name = "event_id", nullable = false)
    private Long event;
    @Column(name = "user_id", nullable = false)
    private Long requester;
    @Column(name = "status", nullable = false)
    private String status;

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
