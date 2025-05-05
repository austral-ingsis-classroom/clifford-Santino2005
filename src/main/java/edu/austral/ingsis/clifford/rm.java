package edu.austral.ingsis.clifford;
public class rm implements Command {

    @Override
    public Result execute(FileManager dirState, String[] parent) {
        FileSystem item = getItem(dirState,parent,null);
        if (item == null) {
            return new Result(dirState, "No such file or directory");
        } else if (parent[0].equals("--recursive")) {
            return recursiveCase(dirState, parent[1], item);
        } else if (item.isDir()) {
            return new Result(dirState, "cannot remove " + parent[0] +", is a directory");
        }
        Directory newDir = dirState.getCurrent().remove(item);
        return new Result(dirState.setDir(newDir), parent[0] + " removed");
    }

    private Result recursiveCase(FileManager dirState, String parent, FileSystem item) {
        if (parent.isEmpty()) {
            Directory childDir = dirState.getCurrent();
            while (childDir != null) {
                FileSystem items = childDir;
                childDir = childDir.getParent();
                if (childDir != null) {
                    childDir.remove(items);
                }
            }
            FileManager newDir = dirState.setDir(childDir);
            return new Result(newDir, "all deleted");
        } else {
            Directory newDir = dirState.getCurrent().remove(item);
            return new Result(dirState.setDir(newDir),parent + " removed");
        }
    }
    private FileSystem getItem(FileManager dirState, String[] parent, FileSystem item){
        for (FileSystem data : dirState.getCurrent().listContent()) {
            if (parent.length > 1 && data.getName().equals(parent[1])) {
                item = data;
                break;
            }
            else if (data.getName().equals(parent[0])) {
                item = data;
                break;
            }
        }
        return item;
    }
}
