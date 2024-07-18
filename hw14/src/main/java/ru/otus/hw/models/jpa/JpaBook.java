package ru.otus.hw.models.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book_graph", attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})
public class JpaBook {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator="books_seq")
    @SequenceGenerator(name="books_seq",
            sequenceName="SEQ_BOOKS", allocationSize=10)
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private JpaAuthor author;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private JpaGenre genre;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaBook jpaBook = (JpaBook) o;
        return id == jpaBook.id && Objects.equals(title, jpaBook.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
