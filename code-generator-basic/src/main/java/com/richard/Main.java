package com.richard;

import com.richard.cli.CommandExecutor;

import java.io.File;

import static com.richard.generator.StaticGenerator.copyFilesByHutool;

public class Main {
    public static void main(String[] args) {
        args = new String[]{"generate", "-l", "-a", "-o"};
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}