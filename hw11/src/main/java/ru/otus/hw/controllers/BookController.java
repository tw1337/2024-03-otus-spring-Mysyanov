package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @GetMapping("/api/books")
    public Flux<BookDto> getAllBooks() {
        return bookRepository.findAll().map(bookMapper::toDto);
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBook(@PathVariable(required = false) String id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<ResponseEntity<BookDto>> createBook(@Valid @RequestBody BookCreateDto book) {
        return save(null, book.getTitle(), book.getAuthorId(), book.getGenres())
                .map(b -> new ResponseEntity<>(bookMapper.toDto(b), HttpStatus.CREATED))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PatchMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<BookDto>> updateBook(@PathVariable("id") String id, @Valid @RequestBody BookUpdateDto book) {
        return save(id, book.getTitle(), book.getAuthorId(), book.getGenres())
                .map(b -> new ResponseEntity<>(bookMapper.toDto(b), HttpStatus.CREATED))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id)
                .then(Mono.fromCallable(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    private Mono<Book> save(String id, String title, String authorId, Collection<String> genresIds) {
        var genresFlux = genreRepository.findAllById(genresIds).collectList();
        var authorMono = authorRepository.findById(authorId);

        return Mono.zip(genresFlux, authorMono, (genres, author) -> new Book(id, title, author, genres))
                .flatMap(bookRepository::save);
    }
}
