package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaComment;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class MigrateMongoToH2JobTest {

    private static final String IMPORT_USER_JOB_NAME = "migrateMongoToH2Job";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_USER_JOB_NAME);

        JobParameters parameters = new JobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        var authors = jpaAuthorRepository.findAll();
        assertThat(authors)
                .isNotEmpty()
                .isEqualTo(getExpectedAuthors());

        var genres = jpaGenreRepository.findAll();
        assertThat(genres)
                .isNotEmpty()
                .isEqualTo(getExpectedGenres());

        var books = jpaBookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .isEqualTo(getExpectedBooks());

        var comments = jpaCommentRepository.findAll();
        assertThat(comments)
                .isNotEmpty()
                .isEqualTo(getExpectedComments());
    }

    private List<JpaAuthor> getExpectedAuthors() {
        return List.of(
                new JpaAuthor(202700L, "Author_1"),
                new JpaAuthor(202710L, "Author_2"),
                new JpaAuthor(202720L, "Author_3")
        );
    }

    private List<JpaBook> getExpectedBooks() {
        return List.of(
                new JpaBook(202700L, "BookTitle_1",
                        new JpaAuthor(202700L, "Author_1"), new JpaGenre(202700L, "Genre_1")
                ),
                new JpaBook(202710L, "BookTitle_2",
                        new JpaAuthor(202710L, "Author_2"), new JpaGenre(202710L, "Genre_2")
                ),
                new JpaBook(202720L, "BookTitle_3",
                        new JpaAuthor(202720L, "Author_3"), new JpaGenre(202720L, "Genre_3"))
                );
    }

    private List<JpaComment> getExpectedComments() {
        var book1 = new JpaBook(202700L, "BookTitle_1",
                new JpaAuthor(202700L, "Author_1"), new JpaGenre(202700L, "Genre_1")
        );
        var book2 = new JpaBook(202710L, "BookTitle_2",
                new JpaAuthor(202710L, "Author_2"), new JpaGenre(202710L, "Genre_2")
        );
        var book3 = new JpaBook(202720L, "BookTitle_3",
                new JpaAuthor(202720L, "Author_3"), new JpaGenre(202720L, "Genre_3")
        );
        return List.of(
                new JpaComment(202700L, "First Comment", book1),
                new JpaComment(202710L, "Second Comment", book2),
                new JpaComment(202720L, "Third Comment", book3)
        );
    }

    private List<JpaGenre> getExpectedGenres() {
        return List.of(
                new JpaGenre(202700L, "Genre_1"),
                new JpaGenre(202710L, "Genre_2"),
                new JpaGenre(202720L, "Genre_3")
        );
    }
}
