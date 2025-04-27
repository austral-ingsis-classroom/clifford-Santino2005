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
        FileManager parent = dirState.setDir(dirState.getCurrent().getParent());
        if (parent == null) {
            FileManager newParent = dirState.setDir(dirState.getRoot());
            return new Result(newParent, "moved to directory " + newParent.getCurrent().getPath());
        }
        return new Result(parent, "moved to directory /");


    }

    private Result handlePath(FileManager dirState, String path) {
        String[] parts = path.split("/");
        return goTo(dirState, parts);
    }

    private Result goTo(FileManager dirState, String[] parts) {
        FileManager result = dirState.setDir(dirState.getCurrent());
        for(String part: parts){
            for (FileSystem data: result.getCurrent().listContent()){
                if(data.getName().equals(part) && data.isDir()){
                    result = result.setDir((Directory) data);
                    break;
                }
            }
        }
        return new Result(result, "moved to directory " + result.getCurrent().getPath());
    }

    private Directory goToDir(String[] parts, Directory currentDir) {
        Directory result = currentDir;
        for (int i = 1; i < parts.length; i++) {
            result = goToNextDir(result, parts[i]);
            if (result == null) return null;
        }
        return result;
    }

    private Directory goToNextDir(Directory currentDir, String part) {
        if (part.equals("..")) {
            if (currentDir.getParent() != null) {
                return currentDir.getParent();
            }
            return currentDir;
        } else if (part.isEmpty() || part.equals(".")) {
            return currentDir;
        }
        return findSubDir(currentDir, part);
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