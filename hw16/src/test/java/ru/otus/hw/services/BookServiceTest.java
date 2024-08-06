package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.GenreMapper;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с книгами ")
@DataJpaTest
@Import({AuthorMapper.class, BookMapper.class,
         CommentMapper.class, GenreMapper.class,
         BookServiceImpl.class})
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
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getId()).isEqualTo(1L);
        assertThat(actualBook.getTitle()).isEqualTo("BookTitle_1");
        assertThat(actualBook.getAuthor()).isNotNull();
        assertThat(actualBook.getAuthor().getId()).isEqualTo(1L);
        assertThat(actualBook.getGenres()).isNotEmpty().hasSize(2);
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
        var insertedBook = bookService.insert(new BookCreateDto(null, "Test_Title_book", 1L, Set.of(1L)));
        var dbBook = bookService.findById(insertedBook.getId());
        assertThat(insertedBook)
                .usingRecursiveComparison()
                .isEqualTo(dbBook);
    }

    @DisplayName("должен обновлять книгу")
    @Test
    @Order(4)
    public void shouldUpdateBook() {
        var oldBook = bookService.findById(1L);
        var genreIds = oldBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet());
        var updatedBook = bookService.update(
                new BookUpdateDto(oldBook.getId(), "Updated Title", oldBook.getAuthor().getId(), genreIds));
        var dbBook = bookService.findById(1L);
        assertThat(updatedBook)
                .usingRecursiveComparison()
                .isEqualTo(dbBook);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    @Order(5)
    public void shouldDeleteBookById() {
        bookService.deleteById(1L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.findById(1L));
    }
}

