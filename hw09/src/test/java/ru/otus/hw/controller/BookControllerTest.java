package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.*;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен ")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

        mockMvc.perform(get("/"))
                .andExpect(view().name("book/all"))
                .andExpect(model().attribute("books", books))
                .andExpect(status().isOk());
    }

    @DisplayName("создавать новую книгу")
    @Test
    public void shouldCreateNewBook() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookCreateDto = new BookCreateDto(null, "New Book Title", 1L, Set.of(1L));
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.insert(bookCreateDto)).willReturn(bookDto);

        mockMvc.perform(post("/book/create").flashAttr("book", bookCreateDto))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("обновлять книгу")
    @Test
    public void shouldUpdateBook() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookUpdateDto = new BookUpdateDto(1L, "New Book Title", 1L, Set.of(1L));
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.update(bookUpdateDto)).willReturn(bookDto);

        mockMvc.perform(post("/book/edit").flashAttr("book", bookUpdateDto))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("удалять книгу")
    @Test
    public void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);
        mockMvc.perform(post("/book/delete/1"))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("провалидировать поля книги")
    @Test
    public void shouldValidateBookFields() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookCreateDto = new BookCreateDto(null, "N", 1L, Set.of(1L));//wrong title
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.insert(bookCreateDto)).willReturn(bookDto);

        mockMvc.perform(post("/book/create").flashAttr("book", bookCreateDto))
                .andExpect(view().name("book/edit"));
    }
}
