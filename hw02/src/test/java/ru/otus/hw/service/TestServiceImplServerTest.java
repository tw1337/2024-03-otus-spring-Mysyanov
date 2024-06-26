package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.util.QuestionUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static ru.otus.hw.util.QuestionConstants.FIRST_ANSWER_NUMBER;
import static ru.otus.hw.util.QuestionConstants.INVALID_ANSWER_NUMBER_TEMPLATE;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplServerTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    public void assertTestExecutionWithNoErrors() {
        var questions = List.of(
                new Question("First question", List.of(new Answer("ans1", true), new Answer("ans2", false))),
                new Question("Second question", List.of(new Answer("ans1", false), new Answer("ans2", true)))
        );
        BDDMockito.given(questionDao.findAll()).willReturn(questions);

        for (var question : questions) {
            BDDMockito.given(ioService.readIntForRangeWithPrompt(
                    1, questions.size(), QuestionUtil.formatQuestion(question), String.format(INVALID_ANSWER_NUMBER_TEMPLATE, FIRST_ANSWER_NUMBER, 2)
            )).willReturn(1);
        }

        var student = new Student("Albert", "Einstein");
        var testResult = testService.executeTestFor(student);
        verify(questionDao, times(1)).findAll();

        verify(ioService, times(1)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        verify(ioService, times(1))
                .readIntForRangeWithPrompt(1, 2,
                        "First question:\n1) ans1\n2) ans2\n",
                        "The answer is incorrect. The answer should be a number from 1 to 2."
                );
        verify(ioService, times(1))
                .readIntForRangeWithPrompt(1, 2,
                        "Second question:\n1) ans1\n2) ans2\n",
                        "The answer is incorrect. The answer should be a number from 1 to 2."
                );
        assertThat(testResult.getStudent()).isEqualTo(student);
        assertThatList(testResult.getAnsweredQuestions()).hasSize(2);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
    }
}
