package ru.otus.hw.models.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "genres")
public class JpaGenre {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator="genres_seq")
    @SequenceGenerator(name="genres_seq",
            sequenceName="SEQ_GENRES", allocationSize=10)
    private long id;

    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaGenre jpaGenre = (JpaGenre) o;
        return id == jpaGenre.id && Objects.equals(name, jpaGenre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
