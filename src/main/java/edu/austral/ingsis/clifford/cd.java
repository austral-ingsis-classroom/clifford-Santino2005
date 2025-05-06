package edu.austral.ingsis.clifford;

import java.util.Optional;

public class cd implements Command {

  @Override
  public Result execute(FileManager state, String[] args) {
    if (args == null || args.length != 1 || args[0].isEmpty()) return result(state);
    return cases(state, args[0]);
  }

  private Result cases(FileManager state, String path) {
    if (path.equals(".")) {
      return result(state);
    }
    if (path.equals("..")) {
      return goToParent(state);
    }
    if (path.equals("/")) {
      return result(new FileManager(state.getRoot(), state.getRoot()));
    }
    if (path.startsWith("/")) {
      return Path(rooted(state), path.substring(1));
    }
    return path.contains("/") ? Path(state, path) : goToChild(state, path);
  }

  private FileManager rooted(FileManager state) {
    return new FileManager(state.getRoot(), state.getRoot());
  }

  private Result goToParent(FileManager state) {
    Directory parent = state.getCurrent().getParent();
    if (parent == null) return result(state);
    return result(state.setDir(findDir(state.getRoot(), parent.getPath())));
  }

  private Result goToChild(FileManager state, String name) {
    Optional<FileSystem> maybe = state.getCurrent().getByName(name);
    if (maybe.isEmpty()) return error(state, name + " directory does not exist");
    if (!maybe.get().isDir()) return error(state, name + " is a file");
    return result(state.setDir((Directory) maybe.get()));
  }

  private Result Path(FileManager state, String path) {
    if (path.isEmpty()) return result(state);
    FileManager current = state;
    for (String segment : path.split("/")) {
      if (segment.isEmpty() || segment.equals(".")) continue;
      current = updateCurrent(state, current, segment);
      if (current == null) return error(state, path);
    }
    return result(current);
  }

  private FileManager updateCurrent(FileManager rootState, FileManager current, String segment) {
    if (segment.equals("..")) return goToParent(rootState, current);
    Optional<FileSystem> next = current.getCurrent().getByName(segment);
    if (next.isEmpty() || !next.get().isDir()) return null;
    return current.setDir((Directory) next.get());
  }

  private FileManager goToParent(FileManager rootState, FileManager current) {
    Directory parent = current.getCurrent().getParent();
    if (parent == null) return current;
    return current.setDir(findDir(rootState.getRoot(), parent.getPath()));
  }

  private Directory findDir(Directory root, String path) {
    if (path.equals("/") || path.isEmpty()) return root;
    Directory current = root;
    for (String part : path.split("/")) {
      if (!part.isEmpty()) {
        Optional<FileSystem> child = current.getByName(part);
        if (child.isPresent() && child.get().isDir()) current = (Directory) child.get();
      }
    }
    return current;
  }

  private Result result(FileManager state) {
    return new Result(state, "moved to directory " + state.getCurrent().getPath());
  }

  private Result error(FileManager state, String msg) {
    return new Result(state, msg);
  }
}
