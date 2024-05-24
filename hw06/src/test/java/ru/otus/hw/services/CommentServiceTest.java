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
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями ")
@DataJpaTest
@Import({CommentConverter.class, CommentServiceImpl.class,
        JpaCommentRepository.class, BookConverter.class,
        AuthorConverter.class, GenreConverter.class,
        JpaBookRepository.class, JpaAuthorRepository.class,
        JpaGenreRepository.class, BookServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @DisplayName("должен найти комментарий по id")
    @Test
    @Order(1)
    public void shouldFindCommentById() {
        var actualComment = commentService.findById(1L);
        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getId()).isEqualTo(1L);
        assertThat(actualComment.get().getBook()).isNotNull();

        var book = actualComment.get().getBook();
        assertThat(book).isNotNull();
        assertThat(book.getId()).isEqualTo(1L);
    }

    @DisplayName("должен найти комментарий по id книги")
    @Test
    @Order(2)
    public void shouldFindCommentByBookId() {
        var actualComments = commentService.findByBookId(1L);
        assertThat(actualComments).isNotEmpty().hasSize(3);
        assertThat(actualComments).allMatch(b -> b.getId() != 0L);
    }

    @DisplayName("должен добавлять новый комментарий")
    @Test
    @Order(3)
    public void shouldAddNewComment() {
        var insertedComment = commentService.insert("Such a nice book", 1L);
        var dbComment = commentService.findById(insertedComment.getId());

        assertThat(dbComment).isPresent();
        assertThat(dbComment.get().getId()).isEqualTo(insertedComment.getId());
        assertThat(dbComment.get().getText()).isEqualTo(insertedComment.getText());
        assertThat(dbComment.get().getBook().getId()).isEqualTo(insertedComment.getBook().getId());
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    @Order(4)
    public void shouldUpdateComment() {
        var oldComment = commentService.findById(1L);
        var updatedComment = commentService.update(oldComment.get().getId(), "Such a nice book_2", oldComment.get().getBook().getId());
        var dbComment = commentService.findById(1L);

        assertThat(dbComment).isPresent();
        assertThat(dbComment.get().getId()).isEqualTo(updatedComment.getId());
        assertThat(dbComment.get().getText()).isEqualTo(updatedComment.getText());
        assertThat(dbComment.get().getBook().getId()).isEqualTo(updatedComment.getBook().getId());
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    @Order(5)
    public void shouldDeleteBookById() {
        commentService.deleteById(1L);
        var actualComment = commentService.findById(1L);
        assertThat(actualComment).isEmpty();
    }
}
