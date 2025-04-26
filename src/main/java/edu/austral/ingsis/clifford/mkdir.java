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
            FileManager state = null;
            for(FileSystem data: dirState.getCurrent().listContent()){
                if(!data.getName().equals(dir[0])){
                    state = dirState.setDir(newDir);
                    return new Result(state ,dir[0] + "created");
                }
            }
            return new Result(state, dir[0] + "already created");
        }
    }
}
