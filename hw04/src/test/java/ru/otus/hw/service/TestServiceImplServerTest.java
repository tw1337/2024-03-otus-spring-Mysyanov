package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class TestServiceImplServerTest {

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    @Test
    public void assertTestExecutionWithNoErrors() {
        var questions = List.of(
                new Question("First question", List.of(new Answer("ans1", true), new Answer("ans2", false))),
                new Question("Second question", List.of(new Answer("ans1", false), new Answer("ans2", true)))
        );
        BDDMockito.given(questionDao.findAll()).willReturn(questions);
        BDDMockito.given(ioService.getMessage("TestService.answer.error.incorrectNumber", FIRST_ANSWER_NUMBER, 2))
                .willReturn("The answer is incorrect. The answer should be a number from 1 to 2.");
        for (var question : questions) {
            BDDMockito.given(ioService.readIntForRangeWithPrompt(
                    1, questions.size(), QuestionUtil.formatQuestion(question),
                    ioService.getMessage("TestService.answer.error.incorrectNumber", FIRST_ANSWER_NUMBER, 2)
            )).willReturn(1);
        }

        var student = new Student("Albert", "Einstein");
        var testResult = testService.executeTestFor(student);
        verify(questionDao, times(1)).findAll();

        verify(ioService, times(2)).printLine("");
        verify(ioService, times(1)).printFormattedLineLocalized("TestService.answer.the.questions");
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
