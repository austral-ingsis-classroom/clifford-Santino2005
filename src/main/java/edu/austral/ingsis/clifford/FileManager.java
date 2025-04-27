package edu.austral.ingsis.clifford;

import java.util.Collections;

public class FileManager {
    private Directory root;
    private Directory current;
    private Cli cli;
    public FileManager(){
        this.root = new Directory("",null, Collections.emptyList());
        this.current = root;
    }
    private FileManager(Directory root, Directory current) {
        this.root = root;
        this.current = current;
    }
    public Directory getRoot(){
        return root;
    }
    public Directory getCurrent(){
        return current;
    }
    public FileManager setDir(Directory go){
        return new FileManager(this.root, go);
    }

}
