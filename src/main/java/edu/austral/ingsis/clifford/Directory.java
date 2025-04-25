package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public class Directory implements FileSystem{
    private List<FileSystem> content = new ArrayList<>();
    private String name;
    private Directory parent;

    public Directory(String name, Directory pattern){
        this.name = name;
        this.parent = pattern;
    }
    public void add(FileSystem element){
        content.add(element);
    }
    public Directory getParent(){
        return this.parent;
    }
    public void remove(FileSystem element){
        content.remove(element);
    }
    public boolean isDir(){
        return true;
    }
    public List<FileSystem> listContent(){
        return this.content;
    }
    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public String getPath() {
        if (parent == null){
            return "/";
        }
        String parentPath = parent.getPath();
        return parentPath.equals("/") ? "/" + name : parentPath + "/" + name;
    }
}
