package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.*;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен ")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private BookMapper bookMapper;

    @DisplayName("возвращать все книги")
    @Test
    public void shouldReturnAllBooks() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var books = List.of(
                new BookDto(1L, "Book1 Title", author, genres),
                new BookDto(2L, "Book2 Title", author, genres)
        );
        BDDMockito.given(bookService.findAll()).willReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @DisplayName("возвращать книгу по id")
    @Test
    public void shouldReturnBookById() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var book = new BookDto(1L, "Book1 Title", author, genres);
        BDDMockito.given(bookService.findById(1L)).willReturn(book);

        mockMvc.perform(get("/api/books/%d".formatted(book.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @DisplayName("возвращать 404 при поиске несуществующей книги")
    @Test
    public void shouldReturnNotFoundOnFindById() throws Exception {
        BDDMockito.given(bookService.findById(1L)).willThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/api/books/%d".formatted(1L)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("создавать новую книгу")
    @Test
    public void shouldCreateNewBook() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookCreateDto = new BookCreateDto(null, "New Book Title", 1L, Set.of(1L));
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.insert(bookCreateDto)).willReturn(bookDto);

        var content = post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookCreateDto));

        mockMvc.perform(content)
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bookDto)));
    }

    @DisplayName("обновлять книгу")
    @Test
    public void shouldUpdateBook() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookUpdateDto = new BookUpdateDto(1L, "New Book Title", 1L, Set.of(1L));
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.update(bookUpdateDto)).willReturn(bookDto);

        var content = patch("/api/books/%d".formatted(bookDto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookUpdateDto));

        mockMvc.perform(content)
                .andExpect(status().isAccepted())
                .andExpect(content().json(mapper.writeValueAsString(bookDto)));
    }

    @DisplayName("удалять книгу")
    @Test
    public void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);
        mockMvc.perform(delete("/api/books/%d".formatted(1L)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("провалидировать поля книги")
    @Test
    public void shouldValidateBookFields() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookCreateDto = new BookCreateDto(null, "N", 1L, Set.of(1L));//wrong title
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.insert(bookCreateDto)).willReturn(bookDto);

        var content = post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookCreateDto));

        mockMvc.perform(content)
                .andExpect(status().isBadRequest());
    }

    @DisplayName("выбросить EntityNotFoundException при создании книги")
    @Test
    public void shouldThrowEntityNotFoundExceptionOnBookCreation() throws Exception {
        var bookCreateDto = new BookCreateDto(null, "New Book Title", 999L, Set.of(1L));
        BDDMockito.given(bookService.insert(bookCreateDto)).willThrow(new EntityNotFoundException("Author is not found"));

        var content = post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookCreateDto));

        mockMvc.perform(content)
                .andExpect(status().isNotFound());
    }
}
