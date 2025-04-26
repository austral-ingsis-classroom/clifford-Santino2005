package edu.austral.ingsis.clifford;

import java.util.List;

public interface Command {
    public Result execute(FileManager dirState, String[] instruction);
    //Anclar los comandos a la interfaz
}
