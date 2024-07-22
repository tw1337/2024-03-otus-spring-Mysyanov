package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.jpa.JpaBook;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {

    @EntityGraph("book_graph")
    Optional<JpaBook> findById(long id);

    @EntityGraph("book_graph")
    @Nonnull
    List<JpaBook> findAll();

}
