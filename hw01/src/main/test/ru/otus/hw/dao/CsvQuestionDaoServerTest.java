package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvQuestionDaoServerTest {

    @Test
    public void assertQuestionReadExceptionOnNotExistingFile() {
        CsvQuestionDao dao = new CsvQuestionDao(new AppProperties("not_existing_file.csv"));
        assertThrows(QuestionReadException.class, dao::findAll);
    }

    @Test
    public void assertNoExceptionOnExistingFile() {
        CsvQuestionDao dao = new CsvQuestionDao(new AppProperties("questions.csv"));
        assertDoesNotThrow(dao::findAll);
    }
}
