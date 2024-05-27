package ru.otus.hw.commands;

import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class DataBaseConsoleCommands {

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
