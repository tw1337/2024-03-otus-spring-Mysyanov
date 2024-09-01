package ru.otus.hw.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @CircuitBreaker(name = "booksBreaker", fallbackMethod = "getAllBooksFallback")
    @GetMapping("/api/books")
    public List<BookDto> getAllBooks(Model model) {
        return bookService.findAll();
    }

    @CircuitBreaker(name = "booksBreaker", fallbackMethod = "getBookFallback")
    @GetMapping("/api/books/{id}")
    public BookDto getBook(@PathVariable(required = false) Long id) {
        return bookService.findById(id);
    }

    @PostMapping("/api/books")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDto createBook(@Valid @RequestBody BookCreateDto book) {
        return bookService.insert(book);
    }

    @PatchMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public BookDto updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookUpdateDto book) {
        book.setId(id);
        return bookService.update(book);
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

    public List<BookDto> getAllBooksFallback(Exception e) {
        log.error("List book error:{}", e.getMessage(), e);
        return new ArrayList<>();
    }

    public BookDto getBookFallback(Exception e) {
        log.error(e.getMessage(), e);
        return new BookDto(null, "No title", null, null);
    }
}
