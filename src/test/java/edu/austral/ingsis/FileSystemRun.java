package edu.austral.ingsis;

import edu.austral.ingsis.clifford.*;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRun implements FileSystemRunner {

  private FileManager fileManager;
  private final Commands commands = new Commands();

  public FileSystemRun() {
    this.fileManager = new FileManager();
  }

  @Override
  public List<String> executeCommands(List<String> commandLines) {
    List<String> results = new ArrayList<>();

    for (String commandLine : commandLines) {
      String[] tokens = commandLine.trim().split(" ");
      if (tokens.length == 0 || tokens[0].isBlank()) continue;

      String commandName = tokens[0];
      String[] args = new String[tokens.length - 1];
      System.arraycopy(tokens, 1, args, 0, args.length);

      Command command = commands.getCommand(commandName);
      if (command != null) {
        Result result = command.execute(fileManager, args);
        fileManager = result.getFileManager();
        results.add(result.getMessage());
      } else {
        results.add("Unknown command: " + commandName);
      }
    }
    return results;
  }
}
