package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями ")
@DataMongoTest
@Import({CommentConverter.class, CommentServiceImpl.class,
        BookConverter.class, AuthorConverter.class,
        GenreConverter.class, BookServiceImpl.class})
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
        var actualComment = commentService.findById("1");
        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getId()).isEqualTo("1");
        assertThat(actualComment.get().getBook()).isNotNull();

        var book = actualComment.get().getBook();
        assertThat(book).isNotNull();
        assertThat(book.getId()).isEqualTo("1");
    }

    @DisplayName("должен найти комментарий по id книги")
    @Test
    @Order(2)
    public void shouldFindCommentByBookId() {
        var actualComments = commentService.findByBookId("1");
        assertThat(actualComments).isNotEmpty().hasSize(1);
        assertThat(actualComments).allMatch(b -> b.getId() != null);
    }

    @DisplayName("должен добавлять новый комментарий")
    @Test
    @Order(3)
    public void shouldAddNewComment() {
        var insertedComment = commentService.insert("Such a nice book", "1");
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
        var oldComment = commentService.findById("1");
        var updatedComment = commentService.update(oldComment.get().getId(), "Such a nice book_2", oldComment.get().getBook().getId());
        var dbComment = commentService.findById("1");

        assertThat(dbComment).isPresent();
        assertThat(dbComment.get().getId()).isEqualTo(updatedComment.getId());
        assertThat(dbComment.get().getText()).isEqualTo(updatedComment.getText());
        assertThat(dbComment.get().getBook().getId()).isEqualTo(updatedComment.getBook().getId());
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    @Order(5)
    public void shouldDeleteBookById() {
        commentService.deleteById("1");
        var actualComment = commentService.findById("1");
        assertThat(actualComment).isEmpty();
    }
}
