package ru.practicum.explore.main.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "compilation")
    @Builder.Default
    private List<Event> events = new ArrayList<>();
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @Column(name = "title", nullable = false)
    private String title;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Compilation that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}