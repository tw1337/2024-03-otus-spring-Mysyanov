package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private Author author1;
    private Author author2;
    private Author author3;

    private Genre genre1;
    private Genre genre2;
    private Genre genre3;
    private Genre genre4;
    private Genre genre5;
    private Genre genre6;

    private Book book1;
    private Book book2;
    private Book book3;

    private Comment comment1;
    private Comment comment2;
    private Comment comment3;

    @ChangeSet(order = "001", id = "dropDb", author = "smysyanov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "smysyanov")
    public void insertAuthors(AuthorRepository authorRepository) {
        author1 = authorRepository.save(new Author("1", "Author_1"));
        author2 = authorRepository.save(new Author("2", "Author_2"));
        author3 = authorRepository.save(new Author("3", "Author_3"));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "smysyanov")
    public void insertGenres(GenreRepository genreRepository) {
        genre1 = genreRepository.save(new Genre("1", "Genre_1"));
        genre2 = genreRepository.save(new Genre("2", "Genre_2"));
        genre3 = genreRepository.save(new Genre("3", "Genre_3"));
        genre4 = genreRepository.save(new Genre("4", "Genre_4"));
        genre5 = genreRepository.save(new Genre("5", "Genre_5"));
        genre6 = genreRepository.save(new Genre("6", "Genre_6"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "smysyanov")
    public void insertBooks(BookRepository bookRepository) {
        book1 = bookRepository.save(
            new Book("1", "BookTitle_1", author1, List.of(genre1, genre2))
        );

        book2 = bookRepository.save(
            new Book("2", "BookTitle_2", author2, List.of(genre3, genre4))
        );

        book3 = bookRepository.save(
            new Book("3", "BookTitle_3", author3, List.of(genre5, genre6))
        );
    }

    @ChangeSet(order = "005", id = "insertComments", author = "smysyanov")
    public void insertComments(CommentRepository commentRepository) {
        comment1 = commentRepository.save(new Comment("1", "First Comment", book1));
        comment2 = commentRepository.save(new Comment("2", "Second Comment", book2));
        comment3 = commentRepository.save(new Comment("3", "Third Comment", book3));
    }
}
