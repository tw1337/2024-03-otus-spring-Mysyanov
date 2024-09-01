package ru.otus.hw.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    @CircuitBreaker(name = "authorsBreaker", fallbackMethod = "getAllAuthorsFallback")
    public List<AuthorDto> getAllAuthors() {
        return authorService.findAll();
    }

    public List<AuthorDto> getAllAuthorsFallback(Exception e) {
        log.error(e.getMessage());
        return new ArrayList<>();
    }
}
