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
@Table(name = "authors")
public class JpaAuthor {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator="authors_seq")
    @SequenceGenerator(name="authors_seq",
            sequenceName="SEQ_AUTHORS", allocationSize=10)
    private long id;

    @Column(name = "full_name")
    private String fullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaAuthor jpaAuthor = (JpaAuthor) o;
        return id == jpaAuthor.id && Objects.equals(fullName, jpaAuthor.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName);
    }
}
