package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.models.Book;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "book")
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph("book_graph")
    Optional<Book> findById(long id);

    @EntityGraph("book_graph")
    @Nonnull
    List<Book> findAll();

    @RestResource(path = "book", rel = "book")
    List<Book> findByTitle(String title);

    @RestResource(path = "delete_book", rel = "delete_book")
    void deleteBookByTitle(String title);
}
