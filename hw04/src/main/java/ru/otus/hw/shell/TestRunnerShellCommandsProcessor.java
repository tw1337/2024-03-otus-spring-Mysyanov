package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.service.TestRunnerService;

@Command
@RequiredArgsConstructor
public class TestRunnerShellCommandsProcessor {

    private final TestRunnerService testRunnerService;

    @Command(command = "run", alias = "r")
    public void run() {
        testRunnerService.run();
    }
}
