package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph("book_graph")
    Optional<Book> findById(long id);

    @EntityGraph("book_graph")
    @Nonnull
    List<Book> findAll();

}
