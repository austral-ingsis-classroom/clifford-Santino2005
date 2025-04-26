package edu.austral.ingsis;


import edu.austral.ingsis.clifford.Cli;
import edu.austral.ingsis.clifford.FileManager;

import java.util.ArrayList;
import java.util.List;

public class FileSystemRun implements FileSystemRunner {

    private FileManager fileManager;
    private Cli cli;

    public FileSystemRun() {
        this.fileManager = new FileManager();
        this.cli = new Cli(fileManager);
    }

    @Override
    public List<String> executeCommands(List<String> commands) {
        List<String> results = new ArrayList<>();

        for (String command : commands) {
            String[] parts = command.split(" ");
            String cmd = parts[0];
            String[] args;

            if (parts.length > 1) {
                args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, args.length);
            } else {
                args = new String[]{""};
            }

            String result;
            switch (cmd) {
                case "cd":
                    result = cli.cd(args);
                    break;
                case "ls":
                    result = cli.ls(args);
                    break;
                case "mkdir":
                    result = cli.mkdir(args);
                    break;
                case "touch":
                    result = cli.touch(args);
                    break;
                case "rm":
                    result = cli.rm(args);
                    break;
                case "pwd":
                    result = cli.pwd();
                    break;
                default:
                    result = "Command not recognized: " + cmd;
            }

            results.add(result);
        }

        return results;
    }
}