package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с книгами ")
@DataJpaTest
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class,
        JpaBookRepository.class, JpaAuthorRepository.class,
        JpaGenreRepository.class, BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceTest {

    @Autowired
    BookService bookService;

    @DisplayName("должен найти книгу по id")
    @Test
    @Order(1)
    public void shouldFindBookById() {
        var actualBook = bookService.findById(1L);
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getId()).isEqualTo(1L);
        assertThat(actualBook.get().getTitle()).isEqualTo("BookTitle_1");
        assertThat(actualBook.get().getAuthor()).isNotNull();
        assertThat(actualBook.get().getAuthor().getId()).isEqualTo(1L);
        assertThat(actualBook.get().getGenres()).isNotEmpty().hasSize(2);
    }

    @DisplayName("должен найти все книги")
    @Test
    @Order(2)
    public void shouldFindAllBooks() {
        var actualBooks = bookService.findAll();
        assertThat(actualBooks).isNotEmpty().hasSize(3);
        assertThat(actualBooks)
                .allMatch(b -> b.getId() != 0L)
                .allMatch(b -> b.getTitle() != null)
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenres() != null && !b.getGenres().isEmpty());
    }

    @DisplayName("должен добавлять новую книгу")
    @Test
    @Order(3)
    public void shouldAddNewBook() {
        var insertedBook = bookService.insert("Test_Title_book", 1L, Set.of(1L));
        var dbBook = bookService.findById(insertedBook.getId());
        assertThat(insertedBook)
                .usingRecursiveComparison()
                .isEqualTo(dbBook.get());
    }

    @DisplayName("должен обновлять книгу")
    @Test
    @Order(4)
    public void shouldUpdateBook() {
        var oldBook = bookService.findById(1L);
        var genreIds = oldBook.get().getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        var updatedBook = bookService.update(oldBook.get().getId(), "Updated Title", oldBook.get().getAuthor().getId(), genreIds);
        var dbBook = bookService.findById(1L);
        assertThat(updatedBook)
                .usingRecursiveComparison()
                .isEqualTo(dbBook.get());
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    @Order(5)
    public void shouldDeleteBookById() {
        bookService.deleteById(1L);
        var actualBook = bookService.findById(1L);
        assertThat(actualBook).isEmpty();
    }
}

