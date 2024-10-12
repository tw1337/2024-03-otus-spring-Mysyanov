package ru.otus.hw.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @CircuitBreaker(name = "commentsBreaker", fallbackMethod = "getCommentByBookIdFallback")
    @GetMapping("/api/books/{id}/comments")
    public List<CommentDto> getCommentByBookId(@PathVariable("id") Long id) {
        return commentService.findByBookId(id);
    }

    public List<CommentDto> getCommentByBookIdFallback(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ArrayList<>();
    }
}
