package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {
    private final Directory root;
    private final Directory current;
    public FileManager(){
        this.root = new Directory("",null, Collections.emptyList());
        this.current = root;
    }
    FileManager(Directory root, Directory current) {
        this.root = root;
        this.current = current;
    }
    public Directory getRoot(){
        return root;
    }
    public Directory getCurrent(){
        return current;
    }
    public FileManager setDir(Directory newCurrent) {
        return new FileManager(root, newCurrent);
    }

    public FileManager updateTree(Directory updated) {
        Directory updatedRoot = update(root, updated);
        Directory updatedCurrent = findDirectory(updatedRoot, current.getPath());
        return new FileManager(updatedRoot, updatedCurrent);
    }

    private Directory update(Directory base, Directory updated) {
        if (base.getPath().equals(updated.getPath())) return updated;
        List<FileSystem> newContent = new ArrayList<>();
        for (FileSystem fs : base.listContent()) {
            if (fs instanceof Directory) {
                newContent.add(update((Directory) fs, updated));
            } else {
                newContent.add(fs);
            }
        }
        return new Directory(base.getName(), base.getParent(), newContent);
    }

    private Directory findDirectory(Directory dir, String path) {
        if (dir.getPath().equals(path)) return dir;

        for (FileSystem fs : dir.listContent()) {
            if (fs instanceof Directory) {
                Directory found = findDirectory((Directory) fs, path);
                if (found != null) return found;
            }
        }

        return null;
    }
}
