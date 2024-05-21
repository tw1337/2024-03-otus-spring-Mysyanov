package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = jpaCommentRepository.findById(1L);
        var expectedComment = em.find(Comment.class, 1L);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарий по book id")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = jpaCommentRepository.findByBookId(1L);
        var expectedComments = getDbComments();

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var book = em.find(Book.class, 1L);
        var newComment = new Comment(0L, "New Comm Text", book);

        jpaCommentRepository.save(newComment);
        Comment actualComment = em.find(Comment.class, newComment.getId());

        assertThat(actualComment).usingRecursiveComparison().isEqualTo(newComment);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdateComment() {
        var comment = em.find(Comment.class, 1L);
        comment.setText("updated text");

        jpaCommentRepository.save(comment);
        var actualComment = em.find(Comment.class, comment.getId());

        assertThat(actualComment).usingRecursiveComparison().isEqualTo(comment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        var comment = em.find(Comment.class, 1L);
        jpaCommentRepository.deleteById(comment.getId());
        var deletedComment = em.find(Comment.class, comment.getId());
        assertThat(deletedComment).isNull();
    }

    private List<Comment> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id -> em.find(Comment.class, id))
                .toList();
    }
}
