package edu.austral.ingsis.clifford;

public class Cli {

    FileManager fm;

    public Cli(FileManager fm) {
        this.fm = fm;
    }

    public String cd(String[] path) {
        cd go = new cd();
        return go.execute(fm, path);
    }
    public String ls(){
        ls content = new ls();
        return null;
    }

    public String touch(String[] fileName){
        touch makeFile = new touch();
        return makeFile.execute(fm, fileName);
    }
    public String mkdir(String[] dirName){
        mkdir makeDir = new mkdir();
        return makeDir.execute(fm, dirName);
    }

}
