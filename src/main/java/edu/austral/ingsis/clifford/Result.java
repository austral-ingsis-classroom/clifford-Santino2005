package edu.austral.ingsis.clifford;

public class Result {

    private final FileManager fileManager;
    private final String message;
    public Result(FileManager fileManager, String message){
        this.fileManager = fileManager;
        this.message = message;
    }

    public FileManager getFileManager(){
        return this.fileManager;
    }
    public String getMessage(){
        return this.message;
    }

}
