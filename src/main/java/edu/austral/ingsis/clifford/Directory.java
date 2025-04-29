package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Directory implements FileSystem{
    private List<FileSystem> content = new ArrayList<>();
    private final String name;
    private final Directory parent;

    public Directory(String name, Directory parent){
        this.name = name;
        this.parent = parent;
    }
    Directory(String name, Directory parent, List<FileSystem> content){
        this.name = name;
        this.parent = parent;
        this.content = Collections.unmodifiableList(content);
    }
    public Directory add(FileSystem element){
        List<FileSystem> newContent = new ArrayList<>(this.content);
        newContent.add(element);
        return new Directory(this.name, this.parent, newContent);
    }
    public Directory getParent(){
        return this.parent;
    }
    public Directory remove(FileSystem element){
        List<FileSystem> newContent = new ArrayList<>(this.content);
        newContent.remove(element);
        return new Directory(this.name, this.parent, newContent);
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
