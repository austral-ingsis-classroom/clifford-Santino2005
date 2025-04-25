package edu.austral.ingsis.clifford;

import java.util.List;

public interface Command {
    public String execute(FileManager dirState, String[] instruction);
    //Anclar los comandos a la interfaz
}
