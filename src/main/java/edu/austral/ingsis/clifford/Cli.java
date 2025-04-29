package edu.austral.ingsis.clifford;

public class Cli {

    private final FileManager fm;

    public Cli(FileManager fm) {
        this.fm = fm;
    }

    public String cd(String[] path) {
        cd go = new cd();
        return go.execute(fm, path).getMessage();
    }
    public String ls(String[] sort){
        ls content = new ls();
        return content.execute(fm, sort).getMessage();
    }

    public String touch(String[] fileName){
        touch makeFile = new touch();
        return makeFile.execute(fm, fileName).getMessage();
    }
    public String mkdir(String[] dirName){
        mkdir makeDir = new mkdir();
        return makeDir.execute(fm, dirName).getMessage();
    }
    public String rm(String[] remove){
        rm delete = new rm();
        return delete.execute(fm, remove).getMessage();
    }

    public String pwd(){
        pwd path = new pwd();
        return path.execute(fm, null).getMessage();
    }
}
