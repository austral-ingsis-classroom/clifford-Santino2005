package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Directory implements FileSystem {
  private final List<FileSystem> content;
  private final String name;
  private final Directory parent;

  public Directory(String name, Directory parent) {
    this.name = name;
    this.parent = parent;
    this.content = new ArrayList<>();
  }

  public Directory(String name, Directory parent, List<FileSystem> content) {
    this.name = name;
    this.parent = parent;
    this.content = List.copyOf(content);
  }

  public Directory add(FileSystem element) {
    List<FileSystem> newContent = new ArrayList<>(this.content);
    newContent.add(element);
    return new Directory(this.name, this.parent, newContent);
  }

  public Directory getParent() {
    return this.parent;
  }

  public Directory remove(FileSystem element) {
    List<FileSystem> newContent = new ArrayList<>(this.content);
    newContent.remove(element);
    return new Directory(this.name, this.parent, newContent);
  }

  public boolean isDir() {
    return true;
  }

  public List<FileSystem> listContent() {
    return this.content;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getPath() {
    if (parent == null) {
      return "/";
    }
    if (parent.getPath().equals("/")) {
      return "/" + name;
    }
    return parent.getPath() + "/" + name;
  }

  public Optional<FileSystem> getByName(String name) {
    return Optional.ofNullable(findByName(name));
  }

  private FileSystem findByName(String name) {
    for (FileSystem c : content) {
      if (c.getName().equals(name)) {
        return c;
      }
    }
    return null;
  }
}
