package edu.austral.ingsis.clifford;

public class mkdir implements Command{

    @Override
    public Result execute(FileManager dirState, String[] dir) {
        if(dir.length > 1){
            return new Result(dirState ,"only one parameter");
        }
        else if(dir[0].equals("/")){
            return new Result( dirState,dir[0] + "cant have /");
        }
        else if(dir[0].contains(" ")){
            return new Result(dirState,dir[0] + "have spaces between");
        }
        else{
            Directory newDir = new Directory(dir[0], dirState.getCurrent());
            return find(dirState, dir[0], newDir);


        }
    }
    private Result find(FileManager dirState, String dir, Directory newDir){
        for(FileSystem data: dirState.getCurrent().listContent()){
            if(data.getName().equals(dir)){
                return new Result(dirState,dir + " directory already created");
            }
        }
        Directory updateDir = dirState.getCurrent().add(newDir);
        return new Result(dirState.setDir(updateDir), dir + " directory created");
    }
}
