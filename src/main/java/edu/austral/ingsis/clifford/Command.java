package edu.austral.ingsis.clifford;

public interface Command {
  public Result execute(FileManager dirState, String[] instruction);
  // Anclar los comandos a la interfaz
}
