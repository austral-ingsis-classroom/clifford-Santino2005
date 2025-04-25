package edu.austral.ingsis.clifford;

public class FileManager {
    private Directory root;
    private Directory current;
    private Cli cli;
    public FileManager(){
        this.root = new Directory("",null);
        this.current = root;
    }
    public Directory getRoot(){
        return root;
    }
    public Directory getCurrent(){
        return current;
    }
    public void setDir(Directory go){
        current = go;
    }
}
