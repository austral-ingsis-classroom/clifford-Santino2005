package edu.austral.ingsis.clifford;

import java.util.HashMap;
import java.util.Map;

public class Commands {
  private final Map<String, Command> commandMap = new HashMap<>();
  private final String basePackage = "edu.austral.ingsis.clifford";

  public Command getCommand(String name) {
    if (commandMap.containsKey(name)) {
      return commandMap.get(name);
    }

    try {
      String className = basePackage + "." + name;
      Class<?> clazz = Class.forName(className);
      Command command = (Command) clazz.getDeclaredConstructor().newInstance();
      commandMap.put(name, command);
      return command;
    } catch (Exception e) {
      return null;
    }
  }
}
