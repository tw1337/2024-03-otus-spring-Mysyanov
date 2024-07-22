package ru.otus.hw.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.processors.BookProcessor;
import ru.otus.hw.processors.CommentProcessor;
import ru.otus.hw.processors.GenreProcessor;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
public class JobConfig {

    private static final int CHUNK_SIZE = 5;

    private static final String MIGRATE_MONGO_TO_H2_JOB_NAME = "migrateMongoToH2Job";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MongoAuthorRepository mongoAuthorRepository;

    @Autowired
    private MongoBookRepository mongoBookRepository;

    @Autowired
    private MongoCommentRepository mongoCommentRepository;

    @Autowired
    private MongoGenreRepository mongoGenreRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job migrateMongoToH2Job(Step authorMigrationStep, Step genreMigrationStep, Step bookMigrationStep, Step commentMigrationStep) {
        return new JobBuilder(MIGRATE_MONGO_TO_H2_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createTemporaryAuthorMongoToH2IdTable())
                .next(createTemporaryGenreMongoToH2IdTable())
                .next(createTemporaryBookMongoToH2IdTable())
                .next(createTemporaryCommentMongoToH2IdTable())
                .next(authorMigrationStep)
                .next(genreMigrationStep)
                .next(bookMigrationStep)
                .next(commentMigrationStep)
                .next(dropTemporaryAuthorMongoToH2IdTable())
                .next(dropTemporaryGenreMongoToH2IdTable())
                .next(dropTemporaryBookMongoToH2IdTable())
                .next(dropTemporaryCommentMongoToH2IdTable())
                .build();
    }

    @Bean
    public TaskletStep createTemporaryAuthorMongoToH2IdTable() {
        return new StepBuilder("createTemporaryAuthorMongoToH2IdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                        "CREATE TABLE temp_author_mongo_h2_ids (id_mongo VARCHAR(255) not null, id_h2 bigserial not null)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep createTemporaryBookMongoToH2IdTable() {
        return new StepBuilder("createTemporaryBookMongoToH2IdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "CREATE TABLE temp_book_mongo_h2_ids (id_mongo VARCHAR(255) not null, id_h2 bigserial not null)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep createTemporaryCommentMongoToH2IdTable() {
        return new StepBuilder("createTemporaryCommentMongoToH2IdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "CREATE TABLE temp_comment_mongo_h2_ids (id_mongo VARCHAR(255) not null, id_h2 bigserial not null)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep createTemporaryGenreMongoToH2IdTable() {
        return new StepBuilder("createTemporaryGenreMongoToH2IdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "CREATE TABLE temp_genre_mongo_h2_ids (id_mongo VARCHAR(255) not null, id_h2 bigserial not null)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTemporaryAuthorMongoToH2IdTable() {
        return new StepBuilder("dropTemporaryAuthorMongoToH2IdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "DROP TABLE temp_author_mongo_h2_ids"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTemporaryBookMongoToH2IdTable() {
        return new StepBuilder("dropTemporaryBookMongoToH2IdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "DROP TABLE temp_book_mongo_h2_ids"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTemporaryCommentMongoToH2IdTable() {
        return new StepBuilder("dropTemporaryCommentMongoToH2IdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "DROP TABLE temp_comment_mongo_h2_ids"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public TaskletStep dropTemporaryGenreMongoToH2IdTable() {
        return new StepBuilder("dropTemporaryGenreMongoToH2IdTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "DROP TABLE temp_genre_mongo_h2_ids"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoAuthor> authorReader() {
        return new RepositoryItemReaderBuilder<MongoAuthor>()
                .name("authorReader")
                .repository(mongoAuthorRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<AuthorDto> authorInsertTempTable() {
        JdbcBatchItemWriter<AuthorDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO temp_author_mongo_h2_ids(id_mongo, id_h2) " +
                "VALUES (:id, SELECT NEXT VALUE FOR SEQ_AUTHORS)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<AuthorDto> authorJdbcBatchItemWriter() {
        JdbcBatchItemWriter<AuthorDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO authors(id, full_name) " +
                "VALUES ((SELECT id_h2 FROM temp_author_mongo_h2_ids WHERE id_mongo = :id), " +
                ":fullName)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<AuthorDto> compositeAuthorWriter(
            JdbcBatchItemWriter<AuthorDto> authorInsertTempTable,
            JdbcBatchItemWriter<AuthorDto> authorJdbcBatchItemWriter) {

        CompositeItemWriter<AuthorDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(authorInsertTempTable, authorJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step authorMigrationStep(RepositoryItemReader<MongoAuthor> reader,
                                    CompositeItemWriter<AuthorDto> writer, AuthorProcessor processor) {
        return new StepBuilder("authorMigrationStep", jobRepository)
                .<MongoAuthor, AuthorDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoBook> bookReader() {
        return new RepositoryItemReaderBuilder<MongoBook>()
                .name("bookReader")
                .repository(mongoBookRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BookDto> bookInsertTempTable() {
        JdbcBatchItemWriter<BookDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO temp_book_mongo_h2_ids(id_mongo, id_h2) " +
                "VALUES (:id, SELECT NEXT VALUE FOR SEQ_BOOKS)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter() {
        JdbcBatchItemWriter<BookDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemPreparedStatementSetter((bookDto, statement) -> {
            statement.setString(1, bookDto.getTitle());
            statement.setString(2, bookDto.getId());
            statement.setString(3, bookDto.getAuthorId());
            statement.setString(4, bookDto.getGenreId());
        });
        writer.setSql("INSERT INTO books(title, id, author_id, genre_id) " +
                "VALUES (?, " +
                "(SELECT id_h2 FROM temp_book_mongo_h2_ids WHERE id_mongo = ?), " +
                "(SELECT id_h2 FROM temp_author_mongo_h2_ids WHERE id_mongo = ?), " +
                "(SELECT id_h2 FROM temp_genre_mongo_h2_ids WHERE id_mongo = ?))");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<BookDto> compositeBookWriter(
            JdbcBatchItemWriter<BookDto> bookInsertTempTable,
            JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter) {

        CompositeItemWriter<BookDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(bookInsertTempTable, bookJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step bookMigrationStep(RepositoryItemReader<MongoBook> reader,
                                  CompositeItemWriter<BookDto> writer, BookProcessor processor) {
        return new StepBuilder("bookMigrationStep", jobRepository)
                .<MongoBook, BookDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoGenre> genreReader() {
        return new RepositoryItemReaderBuilder<MongoGenre>()
                .name("genreReader")
                .repository(mongoGenreRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<GenreDto> genreInsertTempTable() {
        JdbcBatchItemWriter<GenreDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO temp_genre_mongo_h2_ids(id_mongo, id_h2) " +
                "VALUES (:id, SELECT NEXT VALUE FOR SEQ_GENRES)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<GenreDto> genreJdbcBatchItemWriter() {
        JdbcBatchItemWriter<GenreDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO genres(id, name) " +
                "VALUES ((SELECT id_h2 FROM temp_genre_mongo_h2_ids WHERE id_mongo = :id), :name)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<GenreDto> compositeGenreWriter(
            JdbcBatchItemWriter<GenreDto> genreInsertTempTable,
            JdbcBatchItemWriter<GenreDto> genreJdbcBatchItemWriter) {

        CompositeItemWriter<GenreDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(genreInsertTempTable, genreJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step genreMigrationStep(RepositoryItemReader<MongoGenre> reader,
                                  CompositeItemWriter<GenreDto> writer, GenreProcessor processor) {
        return new StepBuilder("genreMigrationStep", jobRepository)
                .<MongoGenre, GenreDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoComment> commentReader() {
        return new RepositoryItemReaderBuilder<MongoComment>()
                .name("commentReader")
                .repository(mongoCommentRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CommentDto> commentInsertTempTable() {
        JdbcBatchItemWriter<CommentDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO temp_comment_mongo_h2_ids(id_mongo, id_h2) " +
                "VALUES (:id, SELECT NEXT VALUE FOR SEQ_COMMENTS)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<CommentDto> commentJdbcBatchItemWriter() {
        JdbcBatchItemWriter<CommentDto> writer = new JdbcBatchItemWriter<>();
        writer.setItemPreparedStatementSetter((commentDto, statement) -> {
            statement.setString(1, commentDto.getText());
            statement.setString(2, commentDto.getId());
            statement.setString(3, commentDto.getBookId());
        });
        writer.setSql("INSERT INTO comments(text, id, book_id) " +
                "VALUES (?, " +
                "(SELECT id_h2 FROM temp_comment_mongo_h2_ids WHERE id_mongo = ?), " +
                "(SELECT id_h2 FROM temp_book_mongo_h2_ids WHERE id_mongo = ?))");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CompositeItemWriter<CommentDto> compositeCommentWriter(
            JdbcBatchItemWriter<CommentDto> commentInsertTempTable,
            JdbcBatchItemWriter<CommentDto> commentJdbcBatchItemWriter) {

        CompositeItemWriter<CommentDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(commentInsertTempTable, commentJdbcBatchItemWriter));
        return writer;
    }

    @Bean
    public Step commentMigrationStep(RepositoryItemReader<MongoComment> reader,
                                   CompositeItemWriter<CommentDto> writer, CommentProcessor processor) {
        return new StepBuilder("commentMigrationStep", jobRepository)
                .<MongoComment, CommentDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public AuthorProcessor authorProcessor() {
        return new AuthorProcessor();
    }

    @Bean
    public GenreProcessor genreProcessorProcessor() {
        return new GenreProcessor();
    }

    @Bean
    public BookProcessor bookProcessorProcessor() {
        return new BookProcessor();
    }

    @Bean
    public CommentProcessor commentProcessorProcessor() {
        return new CommentProcessor();
    }
}
