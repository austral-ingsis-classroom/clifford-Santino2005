package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class touch implements Command {

  @Override
  public Result execute(FileManager dirState, String[] file) {
    if (!validArgs(file)) {
      return new Result(dirState, "file name is required");
    } else if (hasSlash(file[0])) {
      return new Result(dirState, file[0] + " cant have /");
    } else if (hasSpaces(file[0])) {
      return new Result(dirState, file[0] + " have spaces between");
    }
    return createFile(dirState, file[0]);
  }

  private boolean validArgs(String[] args) {
    return args != null && args.length > 0 && !args[0].isEmpty();
  }

  private boolean hasSlash(String name) {
    return name.equals("/");
  }

  private boolean hasSpaces(String name) {
    return name.contains(" ");
  }

  private Result createFile(FileManager state, String name) {
    File newFile = new File(name, state.getCurrent());
    Directory updated = state.getCurrent().add(newFile);

    Directory newRoot = updateTree(state.getRoot(), state.getCurrent().getPath(), updated);
    Directory newCurrent = findDir(newRoot, state.getCurrent().getPath());

    return new Result(new FileManager(newRoot, newCurrent), name + " file created");
  }

  private Directory updateTree(Directory root, String targetPath, Directory updated) {
    if (root.getPath().equals(targetPath)) return updated;
    return updateChildren(root, targetPath, updated);
  }

  private Directory updateChildren(Directory dir, String targetPath, Directory updated) {
    List<FileSystem> newContent = new ArrayList<>();
    for (FileSystem item : dir.listContent())
      newContent.add(updatedItem(item, targetPath, updated));
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
