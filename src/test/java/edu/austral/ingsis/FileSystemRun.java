package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class FileSystemRun implements FileSystemRunner {

    private FileManager fileManager;
    private Cli cli;
    private final Commands commands = new Commands();

    public FileSystemRun() {
        this.fileManager = new FileManager();
        this.cli = new Cli(fileManager);
    }

    @Override
    public List<String> executeCommands(List<String> commandLines) {
        List<String> results = new ArrayList<>();
        for (String commandLine : commandLines) {
            String[] tokens = commandLine.trim().split(" ");
            if (tokens.length == 0) continue;

            String commandName = tokens[0];
            String[] args = new String[tokens.length - 1];
            System.arraycopy(tokens, 1, args, 0, args.length);

            Command command = commands.getCommand(commandName);
            if (command != null) {
                Result result = command.execute(fileManager, args);
                results.add(result.toString());
            } else {
                results.add("Unknown command: " + commandName);
            }
        }
        return results;
    }
}