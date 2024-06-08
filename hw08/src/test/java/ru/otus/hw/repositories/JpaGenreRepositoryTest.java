package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataMongoTest
public class JpaGenreRepositoryTest {

    @Autowired
    private GenreRepository jpaGenreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать жанры по id")
    @Test
    void shouldReturnCorrectGenresByIds() {
        List<Genre> expectedGenres = getDbGenres();
        var expectedGenreIds = expectedGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        var actualGenres = jpaGenreRepository.findAllById(expectedGenreIds);
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualGenres = jpaGenreRepository.findAll();
        var expectedGenres = getDbGenres();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    private List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> mongoTemplate.findById(String.valueOf(id), Genre.class))
                .toList();
    }
}
