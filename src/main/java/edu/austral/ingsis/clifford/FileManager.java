package edu.austral.ingsis.clifford;

import java.util.Collections;

public class FileManager {
  private final Directory root;
  private final Directory current;

  public FileManager() {
    this.root = new Directory("", null, Collections.emptyList());
    this.current = root;
  }

  public FileManager(Directory root, Directory current) {
    this.root = root;
    this.current = current;
  }

  public Directory getRoot() {
    return root;
  }

  public Directory getCurrent() {
    return current;
  }

  public FileManager setDir(Directory newCurrent) {
    return new FileManager(this.root, newCurrent);
  }
}
