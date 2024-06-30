package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final BookService bookService;
    private final CommentService commentService;

    @GetMapping("/comment")
    public String getCommentByBookId(@RequestParam("book_id") long id, Model model) {
        var book = bookService.findById(id);
        var comments = commentService.findByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "comment/book_comments";
    }
}
