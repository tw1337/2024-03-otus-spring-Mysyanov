package ru.otus.hw.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен ")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("возращать только 1 и 3 книгу")
    @Test
    @Order(1)
    public void shouldReturnExpectedBooksForUser() throws Exception {
        var author1 = new AuthorDto(1L, "Author_1");
        var author3 = new AuthorDto(3L, "Author_3");
        var genres1 = List.of(
                new GenreDto(1L, "Genre_1"),
                new GenreDto(2L, "Genre_2")
        );
        var genres2 = List.of(
                new GenreDto(5L, "Genre_5"),
                new GenreDto(6L, "Genre_6")
        );
        var books = List.of(
                new BookDto(1L, "BookTitle_1", author1, genres1),
                new BookDto(3L, "BookTitle_3", author3, genres2)
        );

        mockMvc.perform(get("/"))
                .andExpect(view().name("book/all"))
                .andExpect(model().attribute("books", books))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("получить 403 для второй книги")
    @Test
    @Order(2)
    public void shouldReturn403ForSecondBook() throws Exception {
        mockMvc.perform(get("/book/edit/2"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("успешно обновить первую книгу")
    @Test
    @Order(3)
    public void shouldUpdateFirstBook() throws Exception {
        var bookUpdateDto = new BookUpdateDto(1L, "BookTitle_1_Edited", 1L, Set.of(1L, 2L));
        mockMvc.perform(post("/book/edit").with(csrf()).flashAttr("book", bookUpdateDto))
                .andExpect(redirectedUrl("/"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("получить 403 для обновления третьей книги")
    @Test
    @Order(4)
    public void shouldReturn403ForThirdBook() throws Exception {
        var bookUpdateDto = new BookUpdateDto(3L, "BookTitle_3", 3L, Set.of(5L, 6L));
        mockMvc.perform(post("/book/edit").with(csrf()).flashAttr("book", bookUpdateDto))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("получить 403 для удаления книги")
    @Test
    @Order(5)
    public void shouldDeleteBook() throws Exception {
        mockMvc.perform(post("/book/delete/3").with(csrf()))
                .andExpect(status().isForbidden());
    }
}