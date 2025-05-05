package edu.austral.ingsis.clifford;

public class cd implements Command {

    @Override
    public Result execute(FileManager state, String[] args) {
        if (args == null || args.length != 1 || args[0].isEmpty())
            return new Result(state, state.getCurrent().getPath());
        return go(state, args[0]);
    }

    private Result go(FileManager state, String path) {
        if (path.equals(".")) return result(state);
        if (path.equals("..")) return parent(state);
        if (path.equals("/")) return result(new FileManager(state.getRoot(), state.getRoot()));
        if (path.startsWith("/")) return relative(new FileManager(state.getRoot(), state.getRoot()), path.substring(1));
        return path.contains("/") ? relative(state, path) : child(state, path);
    }

    private Result parent(FileManager state) {
        Directory parent = state.getCurrent().getParent();
        return result(parent == null ? state : state.setDir(parent));
    }

    private Result relative(FileManager state, String path) {
        FileManager current = state;
        for (String segment : path.split("/")) {
            if (segment.equals("") || segment.equals(".")) continue;
            if (segment.equals("..")) current = current.getCurrent().getParent() != null ?
                    current.setDir(current.getCurrent().getParent()) : current;
            else {
                var next = current.getCurrent().getByName(segment);
                if (next.isEmpty() || !next.get().isDir()) return error(state, path);
                current = current.setDir((Directory) next.get());
            }
        }
        return result(current);
    }

    private Result child(FileManager state, String name) {
        var maybe = state.getCurrent().getByName(name);
        if (maybe.isEmpty()) return new Result(state, name + " directory does not exist");
        if (!maybe.get().isDir()) return new Result(state, name + " is a file");
        return result(state.setDir((Directory) maybe.get()));
    }

    private Result result(FileManager state) {
        return new Result(state, "moved to directory " + state.getCurrent().getPath());
    }

    private Result error(FileManager state, String path) {
        return new Result(state, path + " path not found");
    }
}
