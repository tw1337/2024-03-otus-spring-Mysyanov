package ru.otus.hw.commands;

import org.h2.tools.Console;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final Job importFromDatabaseJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm-jl")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobExecution execution = jobLauncher.run(importFromDatabaseJob, new JobParameters());
        System.out.println(execution);
    }

    @ShellMethod(value = "Open H2 console", key = "oc")
    public String openConsole() {
        try {
            Console.main();
            return "Open H2 console...";
        } catch (Exception ex) {
            return "Error while opening H2 console: %s".formatted(ex.getLocalizedMessage());
        }
    }
}
