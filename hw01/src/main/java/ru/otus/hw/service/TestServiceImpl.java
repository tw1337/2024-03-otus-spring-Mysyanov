package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.util.QuestionUtil;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        var questions = questionDao.findAll();
        if (CollectionUtils.isEmpty(questions)) {
            //some log here
            return;
        }

        questions.forEach(question -> ioService.printLine(QuestionUtil.formatQuestion(question)));
    }
}
