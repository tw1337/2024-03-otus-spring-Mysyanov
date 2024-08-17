package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class JpaBookRestRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnAllBooks() throws Exception {
        mockMvc.perform(get("/sdr/book"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnFirstBook() throws Exception {
        mockMvc.perform(get("/sdr/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("BookTitle_1"));
    }
}
