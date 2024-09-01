package ru.otus.hw.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @CircuitBreaker(name = "genresBreaker", fallbackMethod = "getAllGenresFallback")
    @GetMapping("/api/genres")
    public List<GenreDto> getAllGenres() {
        return genreService.findAll();
    }

    public List<GenreDto> getAllGenresFallback(Exception ex) {
        log.error(ex.getMessage());
        return new ArrayList<>();
    }
}
