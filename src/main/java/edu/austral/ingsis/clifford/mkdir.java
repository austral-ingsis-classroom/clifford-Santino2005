package edu.austral.ingsis.clifford;

import java.util.Optional;

public class mkdir implements Command {
    @Override
    public Result execute(FileManager fm, String[] args) {
        if (args.length != 1) return new Result(fm, "mkdir needs 1 arg");
        String name = args[0];
        if (name.contains("/") || name.contains(" "))
            return new Result(fm, "invalid name: " + name);
        if (fm.getCurrent().getByName(name).isPresent())
            return new Result(fm, "dir already exists: " + name);
        Directory added = fm.getCurrent().add(new Directory(name, fm.getCurrent()));
        Directory newRoot = rebuild(fm.getRoot(), fm.getCurrent(), added);
        Directory newCurrent = find(newRoot, fm.getCurrent().getPath());
        return new Result(new FileManager(newRoot, newCurrent), name + " directory created");
    }

    private Directory rebuild(Directory root, Directory target, Directory updated) {
        if (root.equals(target)) return updated;
        var updatedChildren = root.listContent().stream()
                .map(c -> c.equals(target) ? updated :
                        c instanceof Directory ? rebuild((Directory) c, target, updated) : c)
                .toList();
        return new Directory(root.getName(), root.getParent(), updatedChildren);
    }

    private Directory find(Directory root, String path) {
        if (path.equals("/") || path.isEmpty()) return root;
        String[] parts = path.split("/");
        Directory curr = root;
        for (String part : parts)
            if (!part.isEmpty())
                curr = (Directory) curr.getByName(part).orElseThrow();
        return curr;
    }
}
