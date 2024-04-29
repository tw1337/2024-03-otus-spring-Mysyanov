package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoServerTest {

    @Mock
    private AppProperties appProperties;

    private QuestionDao questionDao;

    @BeforeEach
    public void initEach() {
        questionDao = new CsvQuestionDao(appProperties);
    }

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
