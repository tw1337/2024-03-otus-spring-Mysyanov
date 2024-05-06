package ru.otus.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.annotation.EnableCommand;
import ru.otus.hw.shell.TestRunnerShellCommandsProcessor;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
@EnableCommand(TestRunnerShellCommandsProcessor.class)
public class AppConfig {
}
