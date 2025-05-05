package edu.austral.ingsis.clifford;

public class mkdir implements Command{

    @Override
    public Result execute(FileManager dirState, String[] args) {
        if (args.length != 1) return new Result(dirState, "only one parameter");
        if (args[0].equals("/")) return new Result(dirState, args[0] + " can't have /");
        if (args[0].contains(" ")) return new Result(dirState, args[0] + " have spaces between");

        Directory newDir = new Directory(args[0], dirState.getCurrent());
        return find(dirState, args[0], newDir);
    }

    private Result find(FileManager dirState, String name, Directory newDir) {
        if (dirState.getCurrent().hasChild(name)) {
            return new Result(dirState, name + " directory already exists");
        }
        Directory updatedDir = dirState.getCurrent().add(newDir);
        return new Result(dirState.updateTree(updatedDir), name + " directory created");
    }
}
