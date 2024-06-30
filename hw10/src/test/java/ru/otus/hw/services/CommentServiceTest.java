package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.GenreMapper;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями ")
@DataJpaTest
@Import({AuthorMapper.class, BookMapper.class,
         CommentMapper.class, GenreMapper.class,
         CommentServiceImpl.class, BookServiceImpl.class})
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
        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getId()).isEqualTo(1L);
        assertThat(actualComment).isNotNull();
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
        var comment = new CommentCreateDto(null, "Such a nice book", 1L);
        var insertedComment = commentService.insert(comment);
        var dbComment = commentService.findById(insertedComment.getId());

        assertThat(dbComment).isNotNull();
        assertThat(dbComment.getId()).isEqualTo(insertedComment.getId());
        assertThat(dbComment.getText()).isEqualTo(insertedComment.getText());
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    @Order(4)
    public void shouldUpdateComment() {
        var oldComment = commentService.findById(1L);
        var comment = new CommentUpdateDto(oldComment.getId(), "Such a nice book_2", 1L);
        var updatedComment = commentService.update(comment);
        var dbComment = commentService.findById(1L);

        assertThat(dbComment).isNotNull();
        assertThat(dbComment.getId()).isEqualTo(updatedComment.getId());
        assertThat(dbComment.getText()).isEqualTo(updatedComment.getText());
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    @Order(5)
    public void shouldDeleteBookById() {
        commentService.deleteById(1L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.findById(1L));
    }
}
