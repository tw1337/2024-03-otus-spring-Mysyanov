package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class CsvQuestionDaoServerTest {

    @MockBean
    private AppProperties appProperties;

    @Autowired
    private QuestionDao questionDao;

    @Test
    public void assertQuestionReadExceptionOnNotExistingFile() {
        //no path to the source file
        assertThrows(QuestionReadException.class, questionDao::findAll);
    }

    @Test
    public void assertNoExceptionOnCorrectFile() {
        Mockito.when(appProperties.getTestFileName()).thenReturn("questions_correct_format.csv");
        var questions = questionDao.findAll();
        assertThatList(questions).hasSize(2);
        assertThat(questions.get(0)).satisfies(question -> assertThat(question.text()).isEqualTo("Correct question one?"));
        assertThat(questions.get(0).answers())
                .satisfiesAnyOf(
                        answers -> assertThat(answers.get(0).text()).isEqualTo("True"),
                        answers -> assertThat(answers.get(1).text()).isEqualTo("False")
                );
        assertThat(questions.get(1)).satisfies(question -> assertThat(question.text()).isEqualTo("Correct question two?"));
        assertThat(questions.get(1).answers())
                .satisfiesAnyOf(
                        answers -> assertThat(answers.get(0).text()).isEqualTo("True"),
                        answers -> assertThat(answers.get(1).text()).isEqualTo("False")
                );
    }

    @Test
    public void assertExceptionOnIncorrectFile() {
        Mockito.when(appProperties.getTestFileName()).thenReturn("questions_incorrect_format.csv");
        assertThrows(QuestionReadException.class, questionDao::findAll);
    }
}
