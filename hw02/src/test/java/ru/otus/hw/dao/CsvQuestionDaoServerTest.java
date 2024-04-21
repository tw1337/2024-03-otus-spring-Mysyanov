package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.exceptions.QuestionReadException;

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
        assertThatList(questionDao.findAll()).hasSize(2);
    }

    @Test
    public void assertExceptionOnIncorrectFile() {
        Mockito.when(appProperties.getTestFileName()).thenReturn("questions_incorrect_format.csv");
        assertThrows(QuestionReadException.class, questionDao::findAll);
    }
}
