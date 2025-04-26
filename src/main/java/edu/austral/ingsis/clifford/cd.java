package edu.austral.ingsis.clifford;

import java.util.Optional;
public class cd implements Command {
    @Override
    public Result execute(FileManager dirState, String[] path) {
        if (path.length > 1) {
            return new Result(dirState, "only use one path");}
        else if (path[0].equals("..")) {
            return handleParentDir(dirState);
        } else if(path[0].equals(".")) {
            return new Result(dirState, dirState.getCurrent().getPath());}
        else if (path[0].contains("/")) {
            return handlePath(dirState, path[0]);}
        return handleSimplePath(dirState,path[0]);
    }

    private Result handleParentDir(FileManager dirState){
        return Optional.ofNullable(dirState.getCurrent().getParent())
                .map(parent -> new Result(dirState.setDir(parent), parent.getPath()))
                .orElse(new Result(dirState, dirState.getCurrent().getPath()));
    }
    private Result handlePath(FileManager dirState, String path){
        Directory currentDir = path.startsWith("/") ? dirState.getRoot() : dirState.getCurrent();
        String[] parts = path.split("/");

        return goToDir(parts, currentDir)
                .map(dir -> new Result(dirState.setDir(dir), "Moved to:" + dir.getPath()))
                .orElse(new Result(dirState, "No such file or directory " + path));
    }
    private Optional<Directory> goToNextDir(Directory currentDir, String part) {
        if (part.equals("..")) {
            return Optional.ofNullable(currentDir.getParent()).or(() -> Optional.of(currentDir));
        }
        if (part.isEmpty() || part.equals(".")) {
            return Optional.of(currentDir);
        }
        return findSubDir(currentDir, part);

    }
    private Optional<Directory> goToDir(String[] parts, Directory currentDir) {
        Optional<Directory> result = Optional.of(currentDir);
        for (String index : parts) {
            result = result.flatMap(dir -> goToNextDir(dir, index));
        }
        return result;
    }
    private Optional<Directory> findSubDir(Directory currentDir, String part){
        return currentDir.listContent().stream()
                .filter(item -> matchDir(item, part))
                .findFirst()
                .filter(FileSystem::isDir)
                .map(item -> (Directory) item);
    }
    private boolean matchDir(FileSystem item, String name){
        return item.getName().equals(name) && item.isDir();
    }
    private Result handleSimplePath(FileManager dirState, String path) {
        return findSubDir(dirState.getCurrent(), path)
                .map(dir -> new Result(dirState.setDir(dir), "Move to:" + dir.getName()))
                .orElseGet(() ->
                        isFile(dirState.getCurrent(), path)
                                ? new Result(dirState, path + " is a file")
                                : new Result(dirState, "No such Directory")
                );
    }
    private boolean isFile(Directory dirState, String name){
        return dirState.listContent().stream()
                .anyMatch(item -> item.getName().equals(name) && !item.isDir());
    }
}