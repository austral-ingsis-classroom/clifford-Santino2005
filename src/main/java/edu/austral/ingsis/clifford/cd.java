package edu.austral.ingsis.clifford;

public class cd implements Command {

    @Override
    public Result execute(FileManager dirState, String[] path) {
        if (path == null || path.length == 0 || path[0].isEmpty()) {
            return new Result(dirState, dirState.getCurrent().getPath());
        }
        if (path.length > 1) {
            return new Result(dirState, "only use one path");
        }
        return go(dirState, path[0]);
    }

    private Result go(FileManager dirState, String path) {
        if (path.equals("..")) {
            return navigateToParent(dirState);
        }
        if (path.equals(".")) {
            return new Result(dirState, dirState.getCurrent().getPath());
        }
        if (path.startsWith("/")) {
            return navigateAbsolute(dirState, path);
        }
        if (path.contains("/")) {
            return navigateRelativePath(dirState, path.split("/"));
        }
        return navigateToChild(dirState, path);
    }

    private Result navigateToParent(FileManager dirState) {
        Directory current = dirState.getCurrent();
        Directory parent = current.getParent();

        if (parent == null) {
            return new Result(dirState, "moved to directory " + current.getPath());
        }

        FileManager newManager = dirState.setDir(parent);
        return new Result(newManager, "moved to directory " + parent.getPath());
    }

    private Result navigateAbsolute(FileManager dirState, String path) {
        FileManager rootManager = new FileManager(dirState.getRoot(), dirState.getRoot());

        if (path.equals("/")) {
            return new Result(rootManager, "moved to directory /");
        }

        String relativePath = path.substring(1);
        return navigateRelativePath(rootManager, relativePath.split("/"));
    }

    private Result navigateRelativePath(FileManager dirState, String[] segments) {
        Result result = new Result(dirState, "");
        FileManager currentManager = dirState;

        for (String segment : segments) {
            if (segment.isEmpty()) continue;
            result = navigateSegment(currentManager, segment);
            if (isNavigationFailed(result)) {
                return new Result(dirState, String.join("/", segments) + " path not found");
            }
            currentManager = result.getFileManager();
        }

        return result;
    }

    private Result navigateSegment(FileManager dirState, String segment) {
        if (segment.equals(".")) {
            return new Result(dirState, dirState.getCurrent().getPath());
        }
        if (segment.equals("..")) {
            return navigateToParent(dirState);
        }
        return navigateToChild(dirState, segment);
    }

    private boolean isNavigationFailed(Result result) {
        String message = result.getMessage();
        return message.contains("does not exist") || message.contains("is a file");
    }

    private Result navigateToChild(FileManager dirState, String dirName) {
        Directory current = dirState.getCurrent();
        FileSystem target = findFileSystem(current, dirName);

        if (target == null) {
            return new Result(dirState, dirName + " directory does not exist");
        }

        if (!target.isDir()) {
            return new Result(dirState, dirName + " is a file");
        }

        FileManager newManager = dirState.setDir((Directory) target);
        return new Result(newManager, "moved to directory " + target.getName());
    }

    private FileSystem findFileSystem(Directory dir, String name) {
        for (FileSystem item : dir.listContent()) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}