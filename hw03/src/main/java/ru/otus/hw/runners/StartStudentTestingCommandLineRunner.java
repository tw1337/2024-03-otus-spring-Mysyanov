package ru.otus.hw.runners;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.TestRunnerService;

@Component
@RequiredArgsConstructor
public class StartStudentTestingCommandLineRunner implements CommandLineRunner {

    private final TestRunnerService testRunnerService;

    @Override
    public void run(String... args) throws Exception {
        testRunnerService.run();
    }
}
