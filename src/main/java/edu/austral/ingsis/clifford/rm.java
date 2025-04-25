package edu.austral.ingsis.clifford;
public class rm implements Command{
    @Override
    public String execute(FileManager dirState, String[] parent) {
        for(FileSystem data: dirState.getCurrent().listContent()){
            if(data.getName().equals(parent[0]) && !data.isDir()){
                dirState.getCurrent().listContent().remove(data);
            }
            else if(data.getName().equals(parent[0]) && data.isDir()){
                return parent + "is a directory, use --recursive";
            }
        }
        return " cannot remove" + parent + ": No such file or directory";
    }
}
