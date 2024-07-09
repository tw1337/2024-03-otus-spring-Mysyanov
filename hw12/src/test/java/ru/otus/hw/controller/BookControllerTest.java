package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
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

    @DisplayName("возвращать 401 для запроса всех книг")
    @Test
    public void shouldReturn401AllBooks() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("создавать новую книгу")
    @Test
    public void shouldCreateNewBook() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookCreateDto = new BookCreateDto(null, "New Book Title", 1L, Set.of(1L));
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.insert(bookCreateDto)).willReturn(bookDto);

        mockMvc.perform(post("/book/create").with(csrf()).flashAttr("book", bookCreateDto))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("возвращать 401 для страницы создания новой книги")
    @Test
    public void shouldReturn401CreateNewBook() throws Exception {
        mockMvc.perform(get("/book/create"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("обновлять книгу")
    @Test
    public void shouldUpdateBook() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookUpdateDto = new BookUpdateDto(1L, "New Book Title", 1L, Set.of(1L));
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.update(bookUpdateDto)).willReturn(bookDto);

        mockMvc.perform(post("/book/edit").with(csrf()).flashAttr("book", bookUpdateDto))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("возвращать 401 для страницы редактирования книги")
    @Test
    public void shouldReturn401ReturnBook() throws Exception {
        mockMvc.perform(get("/book/edit"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("удалять книгу")
    @Test
    public void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);
        mockMvc.perform(post("/book/delete/1").with(csrf()))
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("возвращать 401 для удаления книги")
    @Test
    public void shouldReturn401DeleteBook() throws Exception {
        mockMvc.perform(post("/book/delete/1").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("провалидировать поля книги")
    @Test
    public void shouldValidateBookFields() throws Exception {
        var author = new AuthorDto(1L, "Author2 Name");
        var genres = List.of(new GenreDto(1L, "Genre1 Name"));
        var bookCreateDto = new BookCreateDto(null, "N", 1L, Set.of(1L));//wrong title
        var bookDto = new BookDto(1L, "New Book Title", author, genres);
        BDDMockito.given(bookService.insert(bookCreateDto)).willReturn(bookDto);

        mockMvc.perform(post("/book/create").with(csrf()).flashAttr("book", bookCreateDto))
                .andExpect(view().name("book/edit"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("выбросить EntityNotFoundException при создании книги")
    @Test
    public void shouldThrowEntityNotFoundExceptionOnBookCreation() throws Exception {
        var bookCreateDto = new BookCreateDto(null, "New Book Title", 999L, Set.of(1L));
        BDDMockito.given(bookService.insert(bookCreateDto)).willThrow(new EntityNotFoundException("Author is not found"));

        mockMvc.perform(post("/book/create").with(csrf()).flashAttr("book", bookCreateDto))
                .andExpect(view().name("error/entitynotfound"))
                .andExpect(model().attribute("errorMessage", "Author is not found"));
    }
}
