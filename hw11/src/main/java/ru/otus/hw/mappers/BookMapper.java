package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book toEntity(BookDto dto) {
        var genres = dto.getGenres().stream().map(genreMapper::toEntity).toList();
        return new Book(dto.getId(), dto.getTitle(), authorMapper.toEntity(dto.getAuthor()), genres);
    }

    public BookDto toDto(Book book) {
        var genres = book.getGenres().stream().map(genreMapper::toDto).toList();
        return new BookDto(book.getId(), book.getTitle(), authorMapper.toDto(book.getAuthor()), genres);
    }

    public BookUpdateDto toUpdateDto(BookDto bookDto) {
        var genreIds = bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet());
        return new BookUpdateDto(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor().getId(), genreIds);
    }

    public BookCreateDto toCreateDto(Book book) {
        var genreIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        return new BookCreateDto(null, book.getTitle(), book.getAuthor().getId(), genreIds);
    }

    public BookUpdateDto toUpdateDto(Book book) {
        var genreIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        return new BookUpdateDto(book.getId(), book.getTitle(), book.getAuthor().getId(), genreIds);
    }
}
