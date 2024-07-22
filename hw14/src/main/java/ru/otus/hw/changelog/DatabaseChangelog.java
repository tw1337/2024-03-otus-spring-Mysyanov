package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

@ChangeLog
public class DatabaseChangelog {

    private MongoAuthor mongoAuthor1;
    private MongoAuthor mongoAuthor2;
    private MongoAuthor mongoAuthor3;

    private MongoGenre mongoGenre1;
    private MongoGenre mongoGenre2;
    private MongoGenre mongoGenre3;

    private MongoBook mongoBook1;
    private MongoBook mongoBook2;
    private MongoBook mongoBook3;

    private MongoComment mongoComment1;
    private MongoComment mongoComment2;
    private MongoComment mongoComment3;

    @ChangeSet(order = "001", id = "dropDb", author = "smysyanov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "smysyanov")
    public void insertAuthors(MongoAuthorRepository mongoAuthorRepository) {
        mongoAuthor1 = mongoAuthorRepository.save(new MongoAuthor("1", "Author_1"));
        mongoAuthor2 = mongoAuthorRepository.save(new MongoAuthor("2", "Author_2"));
        mongoAuthor3 = mongoAuthorRepository.save(new MongoAuthor("3", "Author_3"));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "smysyanov")
    public void insertGenres(MongoGenreRepository mongoGenreRepository) {
        mongoGenre1 = mongoGenreRepository.save(new MongoGenre("1", "Genre_1"));
        mongoGenre2 = mongoGenreRepository.save(new MongoGenre("2", "Genre_2"));
        mongoGenre3 = mongoGenreRepository.save(new MongoGenre("3", "Genre_3"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "smysyanov")
    public void insertBooks(MongoBookRepository mongoBookRepository) {
        mongoBook1 = mongoBookRepository.save(
            new MongoBook("1", "BookTitle_1", mongoAuthor1, mongoGenre1)
        );

        mongoBook2 = mongoBookRepository.save(
            new MongoBook("2", "BookTitle_2", mongoAuthor2, mongoGenre2)
        );

        mongoBook3 = mongoBookRepository.save(
            new MongoBook("3", "BookTitle_3", mongoAuthor3, mongoGenre3)
        );
    }

    @ChangeSet(order = "005", id = "insertComments", author = "smysyanov")
    public void insertComments(MongoCommentRepository commentRepository) {
        mongoComment1 = commentRepository.save(new MongoComment("1", "First Comment", mongoBook1));
        mongoComment2 = commentRepository.save(new MongoComment("2", "Second Comment", mongoBook2));
        mongoComment3 = commentRepository.save(new MongoComment("3", "Third Comment", mongoBook3));
    }
}
