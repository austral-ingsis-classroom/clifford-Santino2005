package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class mkdir implements Command {

  @Override
  public Result execute(FileManager dirState, String[] args) {
    if (args == null || args.length == 0) {
      return new Result(dirState, "mkdir needs 1 arg");
    }
    String name = args[0];
    if (invalidName(name)) {
      return new Result(dirState, "invalid name: " + name);
    }
    if (dirExists(dirState, name)) {
      return new Result(dirState, "dir already exists: " + name);
    }
    return createDirectory(dirState, name);
  }

  private boolean invalidName(String name) {
    return name.contains("/") || name.contains(" ");
  }

  private boolean dirExists(FileManager state, String name) {
    return state.getCurrent().getByName(name).isPresent();
  }

  private Result createDirectory(FileManager state, String name) {
    Directory newDir = new Directory(name, state.getCurrent());
    Directory updated = state.getCurrent().add(newDir);
    Directory newRoot = updateTree(state.getRoot(), state.getCurrent().getPath(), updated);
    Directory newCurrent = findDir(newRoot, state.getCurrent().getPath());
    return new Result(new FileManager(newRoot, newCurrent), name + " directory created");
  }

  private Directory updateTree(Directory root, String route, Directory updated) {
    if (root.getPath().equals(route)) return updated;
    return updateChildren(root, route, updated);
  }

  private Directory updateChildren(Directory dir, String route, Directory updated) {
    List<FileSystem> content = new ArrayList<>();
    for (FileSystem item : dir.listContent()) content.add(updatedItem(item, route, updated));
    return new Directory(dir.getName(), dir.getParent(), content);
  }

  private FileSystem updatedItem(FileSystem item, String route, Directory updated) {
    if (item.isDir() && route.startsWith(item.getPath()))
      return updateTree((Directory) item, route, updated);
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
