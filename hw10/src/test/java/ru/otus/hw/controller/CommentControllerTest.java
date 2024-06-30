package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.CommentController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер комментариев должен ")
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("возвращать все коммантарии к книге")
    @Test
    public void shouldReturnCommentsByBookId() throws Exception {
        var author = new AuthorDto(1L, "Author1 Name");
        var genre = new GenreDto(1L, "Genre1 Name");
        var book = new BookDto(1L, "Book1 Name", author, List.of(genre));
        var comments = List.of(new CommentDto(1L, "Comment1 Text"));
        BDDMockito.given(bookService.findById(1L)).willReturn(book);
        BDDMockito.given(commentService.findByBookId(1L)).willReturn(comments);

        mockMvc.perform(get("/api/books/%d/comments".formatted(book.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comments)));
    }
}
