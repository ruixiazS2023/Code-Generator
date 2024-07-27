package com.richard.maker.cli;

import com.richard.maker.cli.command.ConfigCommand;
import com.richard.maker.cli.command.GenerateCommand;
import com.richard.maker.cli.command.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "richard",mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{
    private final CommandLine commandLine;
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }

    @Override
    public void run() {
        System.out.println("Please enter specific command, or --help for usage information.");
    }

    public Integer doExecute(String[] args) {
        return commandLine.execute(args);
    }
}
