package ru.practicum.explore.main.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.explore.main.enums.EventState;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation", nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Builder.Default
    @Column(name = "confirmed_requests", nullable = false)
    private Long confirmedRequests = 0L;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User initiator;
    @Column(name = "lat", nullable = false)
    private Float lat;
    @Column(name = "lon", nullable = false)
    private Float lon;
    @Column(name = "paid", nullable = false)
    private Boolean paid;
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private EventState state;
    @Column(name = "title", nullable = false)
    private String title;
    @ManyToOne
    @JoinColumn(name = "compilation_id")
    private Compilation compilation;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Event that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}