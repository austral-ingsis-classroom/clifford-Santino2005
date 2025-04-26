package edu.austral.ingsis.clifford;

public class cd implements Command {
    @Override
    public Result execute(FileManager dirState, String[] path) {
        if (path.length > 1) {
            return new Result(dirState, "only use one path");
        }
        if (path[0].equals("..")) {
            return handleParentDir(dirState);
        } else if (path[0].equals(".")) {
            return new Result(dirState, dirState.getCurrent().getPath());
        } else if (path[0].contains("/")) {
            return handlePath(dirState, path[0]);
        }
        return handleSimplePath(dirState, path[0]);
    }

    private Result handleParentDir(FileManager dirState) {
        Directory parent = dirState.getCurrent().getParent();
        if (parent == null) {
            return new Result(dirState, dirState.getCurrent().getPath());
        }
        return new Result(dirState.setDir(parent), "moved to directory '/'");
    }

    private Result handlePath(FileManager dirState, String path) {
        Directory currentDir = getStartingDirectory(dirState, path);
        String[] parts = path.split("/");
        int startIndex = getStartIndex(path, parts);
        return goTo(dirState, parts, currentDir, startIndex);
    }

    private Directory getStartingDirectory(FileManager dirState, String path) {
        if (path.startsWith("/")) {
            return dirState.getRoot();
        }
        return dirState.getCurrent();
    }

    private int getStartIndex(String path, String[] parts) {
        if (path.startsWith("/") && parts.length > 0 && parts[0].isEmpty()) {
            return 1;
        }
        return 0;
    }

    private Result goTo(FileManager dirState, String[] parts,
                        Directory currentDir, int startIndex) {
        Directory targetDir = goToDir(parts, currentDir, startIndex);
        if (targetDir == null) {
            return new Result(dirState, "No such file or directory " + String.join("/", parts));
        }
        String dirName = parts[parts.length - 1];
        return new Result(dirState.setDir(targetDir), "moved to directory '" + dirName + "'");
    }

    private Directory goToDir(String[] parts, Directory currentDir, int startIndex) {
        Directory result = currentDir;
        for (int i = startIndex; i < parts.length; i++) {
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