package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер авторов должен ")
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @DisplayName("возвращать всех авторов")
    @Test
    public void shouldReturnAllAuthors() throws Exception {
        var authors = List.of(
                new AuthorDto(1L, "Author1 Name"),
                new AuthorDto(2L, "Author2 Name")
        );
        BDDMockito.given(authorService.findAll())
                .willReturn(authors);

        mockMvc.perform(get("/author/all").flashAttr("authors", authors))
                .andExpect(status().isOk());
    }
}
