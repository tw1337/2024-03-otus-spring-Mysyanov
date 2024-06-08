package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataMongoTest

public class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository jpaCommentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = jpaCommentRepository.findById("1");
        var expectedComment = mongoTemplate.findById("1", Comment.class);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарий по book id")
    @Test
    void shouldReturnCorrectCommentListByBookId() {
        var actualComments = jpaCommentRepository.findByBookId("1");
        var expectedComments = getDbComments();

        assertThat(actualComments).containsExactlyElementsOf(List.of(expectedComments.get(0)));
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var book = mongoTemplate.findById("3", Book.class);
        var newComment = new Comment(null, "New Comm Text", book);

        jpaCommentRepository.save(newComment);
        Comment actualComment = mongoTemplate.findById(newComment.getId(), Comment.class);

        assertThat(actualComment).isEqualTo(newComment);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdateComment() {
        var comment = mongoTemplate.findById("3", Comment.class);
        assertThat(comment).isNotNull();
        comment.setText("updated text");

        jpaCommentRepository.save(comment);
        var actualComment = mongoTemplate.findById(comment.getId(), Comment.class);

        assertThat(actualComment).isEqualTo(comment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        var comment = mongoTemplate.findById("2", Comment.class);
        assertThat(comment).isNotNull();
        jpaCommentRepository.deleteById(comment.getId());
        var deletedComment = mongoTemplate.findById(comment.getId(), Comment.class);
        assertThat(deletedComment).isNull();
    }

    private List<Comment> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id -> mongoTemplate.findById(String.valueOf(id), Comment.class))
                .toList();
    }
}
