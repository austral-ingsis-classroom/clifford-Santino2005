package edu.austral.ingsis.clifford;

import java.util.Optional;

public class cd implements Command {
    @Override
    public Result execute(FileManager dirState, String[] path) {
        if (path == null || path.length == 0 || path[0].isEmpty()) {
            return new Result(dirState, dirState.getCurrent().getPath());
        }
        if (path.length > 1) {
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
        Directory parent = dirState.getCurrent().getParent();
        if (parent == null) {
            return new Result(dirState, dirState.getCurrent().getPath());
        }
        return new Result(dirState.setDir(parent), "moved to directory /");
    }

    private Result handlePath(FileManager dirState, String path) {
        Directory currentDir = path.startsWith("/") ?
                dirState.getRoot() : dirState.getCurrent();
        String[] parts = path.split("/");
        int startIndex = getStartIndex(path, parts);

        if (path.equals("/")) {
            return new Result(dirState.setDir(dirState.getRoot()), "moved to directory /");
        }

        return goTo(dirState, parts, currentDir, startIndex);
    }

    private int getStartIndex(String path, String[] parts) {
        if (path.startsWith("/") && parts.length > 0 && parts[0].isEmpty()) {
            return 1;
        }
        return 0;
    }

    private Result goTo(FileManager dirState, String[] parts, Directory currentDir, int startIndex) {
        if (parts.length == 0 || startIndex >= parts.length) {
            return new Result(dirState.setDir(currentDir), "moved to directory /");
        }

        Optional<Directory> targetDir = findTargetDir(parts, currentDir, startIndex);
        if (targetDir.isEmpty()) {
            return new Result(dirState, "No such file or directory " + String.join("/", parts));
        }

        String dirName = getLastValidPathPart(parts);
        return new Result(dirState.setDir(targetDir.get()),
                "moved to directory '" + dirName + "'");
    }

    private String getLastValidPathPart(String[] parts) {
        for (int i = parts.length - 1; i >= 0; i--) {
            if (!parts[i].isEmpty()) {
                return parts[i];
            }
        }
        return "/";
    }

    private Optional<Directory> findTargetDir(String[] parts, Directory currentDir, int startIndex) {
        Directory result = currentDir;
        for (int i = startIndex; i < parts.length; i++) {
            Optional<Directory> next = getNextDir(result, parts[i]);
            if (next.isEmpty()) {
                return Optional.empty();
            }
            result = next.get();
        }
        return Optional.of(result);
    }

    private Optional<Directory> getNextDir(Directory currentDir, String part) {
        if (part.equals("..")) {
            return Optional.ofNullable(currentDir.getParent())
                    .or(() -> Optional.of(currentDir));
        } else if (part.isEmpty() || part.equals(".")) {
            return Optional.of(currentDir);
        }

        return findSubDir(currentDir, part);
    }

    private Optional<Directory> findSubDir(Directory currentDir, String part) {
        for (FileSystem item : currentDir.listContent()) {
            if (item.getName().equals(part) && item.isDir()) {
                return Optional.of((Directory) item);
            }
        }
        return Optional.empty();
    }

    private Result handleSimplePath(FileManager dirState, String path) {
        Optional<Directory> dir = findSubDir(dirState.getCurrent(), path);
        if (dir.isPresent()) {
            return new Result(dirState.setDir(dir.get()), "moved to directory " + path);
        }

        if (isFile(dirState.getCurrent(), path)) {
            return new Result(dirState, path + " is a file");
        }
        return new Result(dirState, path + " directory does not exist");
    }

    private boolean isFile(Directory dir, String name) {
        for (FileSystem item : dir.listContent()) {
            if (item.getName().equals(name) && !item.isDir()) {
                return true;
            }
        }
        return false;
    }
}