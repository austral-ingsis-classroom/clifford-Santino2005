package edu.austral.ingsis.clifford;

public class cd implements Command {
    @Override
    public Result execute(FileManager dirState, String[] path) {
        if (path == null || path.length == 0 || path[0].isEmpty()) {
            return new Result(dirState, dirState.getCurrent().getPath());
        }
        else if (path.length > 1) {
            return new Result(dirState, "only use one path");
        }
        return cases(dirState, path[0]);
    }
    private Result cases(FileManager dirState, String path) {
        if (path.equals("..")) {
            return handleParentDir(dirState);
        } else if (path.equals(".")) {
            return new Result(dirState, dirState.getCurrent().getPath());
        } else if (path.contains("/")) {
            return handlePath(dirState, path);
        }
        return handleSimplePath(dirState, path);
    }

    private Result handleParentDir(FileManager dirState) {
        if(dirState.getCurrent().getParent() == null){
            return new Result(dirState, "moved to directory " + dirState.getCurrent().getPath());
        }
        Directory parentDir = new Directory(dirState.getCurrent().getParent().getName(),
                dirState.getCurrent().getParent(), dirState.getCurrent().getParent().listContent());
        FileManager parent = new FileManager(dirState.getRoot(), parentDir);
        return new Result(parent, "moved to directory " + parent.getCurrent().getPath());
    }
    private Result handlePath(FileManager dirState, String path) {
        String[] parts = path.split("/");
        if(parts.length == 0){
            return startFromRoot(dirState);
        }
        return goTo(dirState, parts);
    }

    private Result startFromRoot(FileManager dirState){
        Directory current = dirState.getCurrent();

        while(current.getPath() != null){
            current = current.getParent().getParent();
        }
        FileManager result = new FileManager(dirState.getRoot(), current);
        return new Result(result, "moved to directory " + result.getCurrent().getPath());
    }

    private Result goTo(FileManager dirState, String[] parts) {
        FileManager result = new FileManager(dirState.getRoot(), dirState.getCurrent());
        for(String part: parts){
            for (FileSystem data: result.getCurrent().listContent()){
                if(data.getName().equals(part) && data.isDir()){
                    result = result.setDir((Directory) data);
                    break;
                }
            }
        }
        return new Result(result, "moved to directory " + result.getCurrent().getName());
    }

    private Directory findSubDir(Directory currentDir, String part) {
        for (FileSystem item : currentDir.listContent()) {
            if (item.getName().equals(part) && item.isDir()) {
                return (Directory) item;
            }
        }
        return null;
    }

    private Result handleSimplePath(FileManager dirState, String path) {
        Directory dir = findSubDir(dirState.getCurrent(), path);
        if (dir != null) {
            return new Result(dirState.setDir(dir), "moved to directory " + path );
        }
        return handleNotFoundPath(dirState, path);
    }

    private Result handleNotFoundPath(FileManager dirState, String path) {
        if (isFile(dirState.getCurrent(), path)) {
            return new Result(dirState, path + " is a file");
        }
        return new Result(dirState,  path + " directory does not exist");
    }

    private boolean isFile(Directory dirState, String name) {
        for (FileSystem item : dirState.listContent()) {
            if (item.getName().equals(name) && !item.isDir()) {
                return true;
            }
        }
        return false;
    }
}