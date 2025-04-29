package edu.austral.ingsis.clifford;

public class File implements FileSystem {

    private final String name;
    private final Directory parent;

    public File(String name, Directory dir){
        this.name = name;
        this.parent = dir;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        String parentDir = parent.getPath();
        return parentDir.equals("/") ? "/" + name : parentDir + "/" + name;
    }
    public boolean isDir(){
        return false;
    }
}
