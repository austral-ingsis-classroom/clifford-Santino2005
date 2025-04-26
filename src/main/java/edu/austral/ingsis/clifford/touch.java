package edu.austral.ingsis.clifford;
public class touch implements Command{

    @Override
    public Result execute(FileManager dirState, String[] file) {
        if(file[0].equals("/")){
            return new Result(dirState, file[0] + "cant have /");
        }
        else if(file[0].contains(" ")){
            return new Result( dirState,file[0] + "have spaces between");
        }
       Directory newDir = dirState.getCurrent().add(new File(file[0],dirState.getCurrent()));
        return new Result(dirState.setDir(newDir), file[0] + "file created");
    }
}
