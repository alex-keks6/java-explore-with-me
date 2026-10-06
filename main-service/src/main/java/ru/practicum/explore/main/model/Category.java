package ru.practicum.explore.main.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Category that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    // Не совсем понял, как тогда нормально переопределить hashCode и equals. Насколько знаю, поля,
    // используемые в equals и hashCode, должны совпадать. Но почитал в интернете, что использовать для Entity поле id
    // не очень хорошо, потому что при создании объекта у нас поле с id равно null, а после сохранения в бд
    // полю присваивается значение, и получается, что хеш объекта меняется. Предварительно сделал hashCode по id,
    // чтоб у них с equals сравнение было по одному полю.
    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}