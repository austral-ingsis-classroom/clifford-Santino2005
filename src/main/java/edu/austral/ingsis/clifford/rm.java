package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class rm implements Command {

  @Override
  public Result execute(FileManager dirState, String[] parent) {
    if (parent.length == 0) {
      return new Result(dirState, "No file or directory specified");
    }

    boolean recursive = isRecursive(parent);
    String name = extractPath(parent, recursive);

    if (name.isEmpty()) {
      return new Result(dirState, "No file or directory specified");
    }

    Optional<FileSystem> path = dirState.getCurrent().getByName(name);
    if (path.isEmpty()) return new Result(dirState, "No such file or directory");

    if (path.get().isDir() && !recursive) {
      return new Result(dirState, "cannot remove " + name + ", is a directory");
    }

    return buildResult(dirState, name, path.get());
  }

  private boolean isRecursive(String[] args) {
    return args[0].equals("--recursive");
  }

  private String extractPath(String[] args, boolean recursive) {
    if (!recursive) return args[0];
    return args.length > 1 ? args[1] : "";
  }

  private Result buildResult(FileManager state, String name, FileSystem target) {
    Directory updated = state.getCurrent().remove(target);
    Directory newRoot = updateTree(state.getRoot(), state.getCurrent().getPath(), updated);
    Directory newCurrent = findDir(newRoot, state.getCurrent().getPath());

    return new Result(new FileManager(newRoot, newCurrent), name + " removed");
  }

  private Directory updateTree(Directory root, String path, Directory updated) {
    if (root.getPath().equals(path)) return updated;
    return updateChildren(root, path, updated);
  }

  private Directory updateChildren(Directory dir, String path, Directory updated) {
    List<FileSystem> newContent = new ArrayList<>();
    for (FileSystem item : dir.listContent()) newContent.add(updatedItem(item, path, updated));
    return new Directory(dir.getName(), dir.getParent(), newContent);
  }

  private FileSystem updatedItem(FileSystem item, String path, Directory updated) {
    if (item.isDir() && path.startsWith(item.getPath()))
      return updateTree((Directory) item, path, updated);
    return item;
  }

  private Directory findDir(Directory root, String path) {
    if (path.equals("/") || path.isEmpty()) return root;

    Directory current = root;
    for (String part : path.split("/")) current = nextDir(current, part).orElse(current);

    return current;
  }

  private Optional<Directory> nextDir(Directory dir, String name) {
    if (name.isEmpty()) return Optional.empty();
    Optional<FileSystem> child = dir.getByName(name);
    if (child.isPresent() && child.get().isDir()) return Optional.of((Directory) child.get());
    return Optional.empty();
  }
}
