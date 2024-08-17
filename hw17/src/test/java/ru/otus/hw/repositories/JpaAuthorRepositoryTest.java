package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
public class JpaAuthorRepositoryTest {

    @Autowired
    private AuthorRepository jpaAuthorRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var expectedAuthors = getDbAuthors();
        for (var expectedAuthor : expectedAuthors) {
            var actualAuthor = jpaAuthorRepository.findById(expectedAuthor.getId());
            assertThat(actualAuthor).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expectedAuthor);
        }
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = jpaAuthorRepository.findAll();
        var expectedAuthors = getDbAuthors();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    private List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> em.find(Author.class, id))
                .toList();
    }
}
