package ru.otus.hw.healthindicators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.repositories.BookRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookCountHealthIndicator.class,
        properties = { "spring.sql.init.mode=never" })
@EnableAutoConfiguration
@AutoConfigureMockMvc
@Import({BookCountHealthIndicator.class})
public class BookCountHealthIndicatorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void actuatorShouldReturnOk() throws Exception {
        mockMvc.perform(get("/metrics"))
                .andExpect(status().isOk());
    }

    @Test
    public void bookCountHealthIndicatorShouldReturnOk() throws Exception {
        when(bookRepository.count()).thenReturn(100L);
        mockMvc.perform(get("/metrics/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    public void bookCountHealthIndicatorShouldReturnDown() throws Exception {
        when(bookRepository.count()).thenReturn(0L);
        mockMvc.perform(get("/metrics/health"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status").value("DOWN"));
    }
}
