package edu.austral.ingsis;

import edu.austral.ingsis.clifford.Cli;
import edu.austral.ingsis.clifford.FileManager;
import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.pwd;
import edu.austral.ingsis.clifford.mkdir;
import edu.austral.ingsis.clifford.touch;
import edu.austral.ingsis.clifford.rm;
import edu.austral.ingsis.clifford.ls;
import edu.austral.ingsis.clifford.cd;

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

            // Creamos una clase para manejar comandos directamente
            Result result;
            switch (cmd) {
                case "cd":
                    cd cdCommand = new cd();
                    result = cdCommand.execute(fileManager, args);
                    break;
                case "ls":
                    ls lsCommand = new ls();
                    result = lsCommand.execute(fileManager, args);
                    break;
                case "mkdir":
                    mkdir mkdirCommand = new mkdir();
                    result = mkdirCommand.execute(fileManager, args);
                    break;
                case "touch":
                    touch touchCommand = new touch();
                    result = touchCommand.execute(fileManager, args);
                    break;
                case "rm":
                    rm rmCommand = new rm();
                    result = rmCommand.execute(fileManager, args);
                    break;
                case "pwd":
                    pwd pwdCommand = new pwd();
                    result = pwdCommand.execute(fileManager, args);
                    break;
                default:
                    result = new Result(fileManager, "Command not recognized: " + cmd);
            }

            fileManager = result.getFileManager();
            cli = new Cli(fileManager);
            results.add(result.getMessage());
        }

        return results;
    }
}