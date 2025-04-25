package edu.austral.ingsis.clifford;

import java.util.Optional;
import java.util.stream.Stream;
public class cd implements Command {
    @Override
    public String execute(FileManager dirState, String[] path) {
        if (path.length > 1) {
            return "only use one path";
        }
        else if (path[0].equals("..")) {
            return handleParentDir(dirState);

        } else if(path[0].equals(".")) {
            return dirState.getCurrent().getPath();
        }
        else if (path[0].contains("/")) {
            return handlePath(dirState, path[0]);
        }

        return handleSimplePath(dirState,path[0]);
    }

    private String handleParentDir(FileManager dirState){
        Optional.ofNullable(dirState.getCurrent().getParent())
                .ifPresent(dirState::setDir);
        return dirState.getCurrent().getPath();
    }
    private String handlePath(FileManager dirState, String path){
        Directory currentDir = path.startsWith("/") ? dirState.getRoot() : dirState.getCurrent();
        String[] parts = path.split("/");

        return Optional.ofNullable(goToDir(parts, currentDir))
                .map(dir -> {dirState.setDir(dir); return "Moved to:" + dir.getPath();})
                .orElse("No such file or directory " + path);
    }

    private Directory goToNextDir(Directory currentDir, String part) {
        if (part.equals("..")) {
            return Optional.ofNullable(currentDir.getParent()).orElse(currentDir);
        }
        if (part.isEmpty() || part.equals(".")) {
            return currentDir;
        }
        return findSubDir(currentDir, part);

    }
    private Directory goToDir(String[] parts, Directory currentDir) {
        for (String index : parts) {
            currentDir = goToNextDir(currentDir, index);
            if (currentDir == null) {
                return null;
            }
        }
        return currentDir;
    }
    private Directory findSubDir(Directory currentDir, String part){
        return (Directory) Stream.of(currentDir.listContent())
                .filter(item -> matchDir((FileSystem) item, part))
                .findFirst().orElse(null);
    }
    private boolean matchDir(FileSystem item, String name){
        return item.getName().equals(name) && item.isDir();
    }
    private String handleSimplePath(FileManager dirState, String path){
        return Optional.ofNullable(findSubDir(dirState.getCurrent(), path))
                .map(dir -> moveToDir(dirState, dir))
                .orElseGet(() -> {return isFile(dirState.getCurrent(), path)
                        ? path + " is a file"
                        : "No such Directory";});
    }
    private boolean isFile(Directory dirState, String name){
        return Stream.of(dirState.listContent())
                .anyMatch(item -> matchDir((FileSystem) item,name));
    }

    private String moveToDir(FileManager dirState, Directory dir){
        dirState.setDir(dir);
        return "Move to:" + dir.getName();
    }
}